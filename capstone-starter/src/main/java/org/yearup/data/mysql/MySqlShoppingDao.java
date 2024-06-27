package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT cart.quantity, p.* FROM shopping_cart AS cart\n" +
                "JOIN products as p ON p.product_id = cart.product_id \n" +
                "WHERE cart.user_id = ?;";

        ShoppingCart shoppingCart = new ShoppingCart();

        try{
            try(
                    Connection connection = getConnection();
            ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                try(
                        ResultSet resultSet = preparedStatement.executeQuery();
                ){
                    while (resultSet.next()){
                        // create the product
                        int quantity = resultSet.getInt("quantity");
                        int productId = resultSet.getInt("product_id");
                        String name = resultSet.getString("name");
                        BigDecimal price = resultSet.getBigDecimal("price");
                        int categoryId = resultSet.getInt("category_id");
                        String description = resultSet.getString("description");
                        String color = resultSet.getString("color");
                        int stock = resultSet.getInt("stock");
                        boolean isFeatured = resultSet.getBoolean("featured");
                        String imageUrl = resultSet.getString("image_url");
                        Product product = new Product(productId,name,price,categoryId,description,
                                color,stock,isFeatured,imageUrl);
                        // create shopping cart item
                        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                        shoppingCartItem.setQuantity(quantity);
                        shoppingCartItem.setProduct(product);
                        // add to shopping cart
                        shoppingCart.add(shoppingCartItem);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error getting cart");
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCart addItemToCart(int userId, int itemId) {

        ShoppingCart shoppingCart = getByUserId(userId);

        int currentQuantity = 0;
        if(shoppingCart.contains(itemId)){
            currentQuantity = shoppingCart.get(itemId).getQuantity() + 1;
            updateCart(userId,itemId,currentQuantity);
        } else {
            // id,productId,quantity
            String sql = "INSERT INTO shopping_cart VALUES (?,?,1);";
            try{
                try(
                        Connection connection = getConnection();
                ){
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1,userId);
                    preparedStatement.setInt(2,itemId);
                    preparedStatement.executeUpdate();
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Error adding item to cart !!!");
            }
            return getByUserId(userId);
        }
        return shoppingCart;
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?;";
        try{
            try(
                    Connection connection = getConnection();
            ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,userId);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error removing item!!!");
        }
    }

    @Override
    public void updateCart(int userId, int itemId,int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?;";
        try{
            try(
                    Connection connection = getConnection();
            ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,quantity);
                preparedStatement.setInt(2,userId);
                preparedStatement.setInt(3,itemId);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error updating item quantity");
        }
    }


}
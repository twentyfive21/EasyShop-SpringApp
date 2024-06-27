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
        // Create an empty shopping cart object to store the items
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            // Establish a connection to the database
            try (Connection connection = getConnection();) {
                // Prepare the SQL statement with the userId parameter
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);

                // Execute the query and get the result set
                try (ResultSet resultSet = preparedStatement.executeQuery();) {

                    // Loop through the result set to process each row
                    while (resultSet.next()) {
                        // Extract the quantity of the item from the result set
                        int quantity = resultSet.getInt("quantity");

                        // Extract the product details from the result set
                        int productId = resultSet.getInt("product_id");
                        String name = resultSet.getString("name");
                        BigDecimal price = resultSet.getBigDecimal("price");
                        int categoryId = resultSet.getInt("category_id");
                        String description = resultSet.getString("description");
                        String color = resultSet.getString("color");
                        int stock = resultSet.getInt("stock");
                        boolean isFeatured = resultSet.getBoolean("featured");
                        String imageUrl = resultSet.getString("image_url");

                        // Create a new Product object using the extracted details
                        Product product = new Product(productId, name, price, categoryId, description,
                                color, stock, isFeatured, imageUrl);

                        // Create a new ShoppingCartItem object and set its quantity and product
                        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                        shoppingCartItem.setQuantity(quantity);
                        shoppingCartItem.setProduct(product);

                        // Add the ShoppingCartItem to the shopping cart
                        shoppingCart.add(shoppingCartItem);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting cart");
        }
        // Return the populated shopping cart
        return shoppingCart;
    }


    @Override
    public ShoppingCart addItemToCart(int userId, int itemId) {
        // Get the shopping cart for the user with the specified userId
        ShoppingCart shoppingCart = getByUserId(userId);
        int currentQuantity = 0;

        // Check if the shopping cart already contains the item with the specified itemId
        if(shoppingCart.contains(itemId)){

            // If the item is already in the cart, get the current quantity and increase it by 1
            currentQuantity = shoppingCart.get(itemId).getQuantity() + 1;
            // Update the quantity of the item in the shopping cart
            shoppingCart.get(itemId).setQuantity(currentQuantity);
            // Update the cart in the database with the new quantity
            updateCart(userId, itemId, currentQuantity);
        } else {
            // If the item is not in the cart, add it to the cart with a quantity of 1
            // Prepare an SQL statement to insert the new item into the shopping_cart table
            String sql = "INSERT INTO shopping_cart VALUES (?,?,1);";
            try {
                // Establish a connection to the database
                try (Connection connection = getConnection();) {

                    // Prepare the SQL statement with the userId and itemId
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setInt(2, itemId);

                    // Execute the SQL statement to insert the new item
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adding item to cart !!!");
            }
            // Return the updated shopping cart for the user
            return getByUserId(userId);
        }
        // Return the updated shopping cart
        return shoppingCart;
    }


    @Override
    public void clearCart(int userId) {
        // SQL query to delete all items from the shopping cart for the specified userId
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?;";

        try {
            // Establish a connection to the database
            try (Connection connection = getConnection();) {

                // Prepare the SQL statement with the userId parameter
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);

                // Execute the SQL statement to delete the items
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error removing item!!!");
        }
    }


    @Override
    public void updateCart(int userId, int itemId, int quantity) {
        // SQL query to update the quantity of a specific item in the shopping cart for the specified userId and itemId
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?;";

        try {
            // Establish a connection to the database
            try (Connection connection = getConnection();) {

                // Prepare the SQL statement with the quantity, userId, and itemId parameters
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, quantity);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, itemId);

                // Execute the SQL statement to update the quantity
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating item quantity");
        }
    }



}
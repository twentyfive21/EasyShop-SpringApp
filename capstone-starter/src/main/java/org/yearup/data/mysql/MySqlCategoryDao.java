package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        String sql = "SELECT * FROM categories;";
        List<Category> allCategories = new ArrayList<>();
        try{
            try(
                    Connection connection = getConnection();
                    ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try(
                        ResultSet resultSet = preparedStatement.executeQuery();
                        ){
                    while(resultSet.next()){
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String desc = resultSet.getString("description");
                    Category category = new Category(categoryId,name,desc);
                    allCategories.add(category);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error getting all categories");
        }
        return allCategories;
    }

    @Override
    public Category getById(int categoryId)
    {
        Category categoryMatch = null;
        // get category by id
        String sql = "SELECT * FROM categories WHERE category_id = ?;";
        try{
            try(
                    Connection connection = getConnection()
                    ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,categoryId);
                try(
                        ResultSet resultSet = preparedStatement.executeQuery();
                        ){
                    while (resultSet.next()){
                        int categoryIdFound = resultSet.getInt("category_id");
                        String name = resultSet.getString("name");
                        String desc = resultSet.getString("description");
                       categoryMatch = new Category(categoryIdFound,name,desc);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error getting item by id");
        }
        return categoryMatch;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql = " INSERT INTO categories (name,description) VALUES (?,?);";
        try{
            try(
                    Connection connection = getConnection();
                    ){
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1,category.getName());
                preparedStatement.setString(2,category.getDescription());

                int rowsAffected = preparedStatement.executeUpdate();

                 if(rowsAffected > 0) {
                     ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                     if(generatedKeys.next()){
                         // Retrieve the auto-incremented ID
                         int newCategoryId = generatedKeys.getInt(1);
                         // get the newly inserted category
                         category.setCategoryId(newCategoryId);
                     }
                 }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error adding new category");
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try{
            String sql = "UPDATE categories SET name = ?, description = ?" +
                    " WHERE category_id = ?;";
            try(
                    Connection connection = getConnection();
                    ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,category.getName());
                preparedStatement.setString(2,category.getDescription());
                preparedStatement.setInt(3,categoryId);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println("Error updating category");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try{
            String sql = "DELETE FROM categories WHERE category_id = ?;";
            try(
                    Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ){
                preparedStatement.setInt(1,categoryId);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error deleting category");
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}

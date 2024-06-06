package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getProfileById(int id) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?;";
        Profile profileMatch = null;
        try{
            try(
                    Connection connection = getConnection()
            ){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                try(
                        ResultSet resultSet = preparedStatement.executeQuery();
                        ){
                    while(resultSet.next()){
                        int userId = resultSet.getInt("user_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String phone = resultSet.getString("phone");
                        String email = resultSet.getString("email");
                        String address = resultSet.getString("address");
                        String city = resultSet.getString("city");
                        String state = resultSet.getString("state");
                        String zip = resultSet.getString("zip");
                        Profile profile = new Profile(userId,firstName,lastName,phone,email,address,city,state,zip);
                        profileMatch = profile;
                    }
                }



            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error getting profile");
        }
        return profileMatch;
    }

    @Override
    public void update(int id, Profile profile) {

    }

}

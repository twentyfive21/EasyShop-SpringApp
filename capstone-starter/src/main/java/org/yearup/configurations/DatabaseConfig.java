package org.yearup.configurations;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This tells Spring that this class contains configuration settings
@Configuration
public class DatabaseConfig {

    // This is a connection pool for managing database connections
    private BasicDataSource basicDataSource;

    // This method returns the data source (database connection) to be used in the application
    @Bean
    public BasicDataSource dataSource() {
        return basicDataSource;
    }

    // This constructor initializes the BasicDataSource with values from the application's properties file
    @Autowired
    public DatabaseConfig(@Value("${datasource.url}") String url,
                          @Value("${datasource.username}") String username,
                          @Value("${datasource.password}") String password) {
        // Create a new BasicDataSource object
        basicDataSource = new BasicDataSource();
        // Set the URL of the database
        basicDataSource.setUrl(url);
        // Set the username to connect to the database
        basicDataSource.setUsername(username);
        // Set the password to connect to the database
        basicDataSource.setPassword(password);
    }

}

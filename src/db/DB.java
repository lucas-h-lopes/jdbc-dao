package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            Properties properties = loadProperties();
            String url = properties.getProperty("dburl");
            connection = DriverManager.getConnection(url, properties);
            return connection;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DbException("Error: " + e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream file = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(file);
            return properties;
        } catch (IOException e) {
            throw new DbException("Error: " + e.getMessage());
        }
    }

    public static void closeStatement(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DbException("Error: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DbException("Error: " + e.getMessage());
            }
        }
    }
}

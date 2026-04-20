package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Manager connection using the configs set in DatabaseConfig.java
this class only ensures a safe connection to the database
*/
public class ConnectionManager {
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.USERNAME,
                DatabaseConfig.PASSWORD
            );
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found. Check lib/ and your classpath.");
            return null;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.out.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}

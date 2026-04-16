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
            return DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.USERNAME,
                DatabaseConfig.PASSWORD
            );
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}

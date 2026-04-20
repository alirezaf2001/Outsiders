package db;
/*
Config for Database access
MUST CHANGE BASED ON THE DATABASE AND LOGIN SYSTEM INDIVIDUALLY
requirements:
DB_URL: url to each team member local database
USERNAME: username for each database
PASSWORD: password for each database
*/
public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/employeeData";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "alireza.f12";

    private DatabaseConfig() {
    }
}

package airlinemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private String url = "jdbc:mysql://localhost:3306/airlinemanagement"; // Update your database URL
    private String user = "root"; // Username
    private String password = "Hacoona_Matata"; // Password

    // Method to establish a database connection
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
        }
        return connection; // Return the established connection
    }
}

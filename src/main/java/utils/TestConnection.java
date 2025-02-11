package utils;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Successfully connected to database!");
            }
        } catch (Exception e) {
            System.err.println("Error testing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
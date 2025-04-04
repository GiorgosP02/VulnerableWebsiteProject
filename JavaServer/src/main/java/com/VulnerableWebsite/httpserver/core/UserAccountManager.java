package com.VulnerableWebsite.httpserver.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAccountManager {

    public static String authenticateUser(String username, String password) {
        String query = "SELECT username, role_id FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("username") + rs.getString("role_id"); // User authenticated (insecurely)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Authentication failed
    }

    public static void createUser(String username, String password, String is_admin) {
        String role_id = is_admin.equals("Yes") ? "1" : "0"; // Corrected string comparison

        String query = "INSERT INTO users VALUES ('" + role_id + "', '" + username + "', '" + password + "')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows inserted: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createUser(String username, String password) {

        String query = "INSERT INTO users VALUES ('0', '" + username + "', '" + password + "')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows inserted: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

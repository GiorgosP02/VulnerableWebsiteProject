package com.VulnerableWebsite.httpserver.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAuthenticator {

    public static String authenticateUser(String username, String password) {
        String query = "SELECT username FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("username"); // User authenticated (insecurely)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Authentication failed
    }
}

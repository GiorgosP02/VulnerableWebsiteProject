package com.VulnerableWebsite.httpserver.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title FROM games")) {
            while (rs.next()) {
                games.add(new Game(rs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public static void addGame(String title) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO games (title) VALUES (?)")) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Game> searchGames(String keyword) {
        List<Game> games = new ArrayList<>();
        /* SAFE
        String query = "SELECT title FROM games WHERE title LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
        */
        String query = "SELECT title FROM games WHERE title LIKE '%" + keyword + "%'"; // Vulnerability ID 1 - SQL Injection (UNION attack)
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                games.add(new Game(rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }
}


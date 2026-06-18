package com.gamehub.reports;

import com.gamehub.db.DatabaseConnection;
import com.gamehub.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    public int totalGamesPlayed(User user) {
        String sql = "SELECT COUNT(*) FROM game_history WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            return 0;
        }
    }

    public String mostPlayedGame(User user) {
        String sql = """
                SELECT game_name, COUNT(*) plays
                FROM game_history
                WHERE user_id = ?
                GROUP BY game_name
                ORDER BY plays DESC
                LIMIT 1
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("game_name") : "No games yet";
            }
        } catch (SQLException ex) {
            return "No games yet";
        }
    }

    public List<String[]> history(User user) {
        List<String[]> rows = new ArrayList<>();
        String sql = """
                SELECT game_name, score, result, DATE_FORMAT(date_played, '%Y-%m-%d %H:%i') played
                FROM game_history
                WHERE user_id = ?
                ORDER BY date_played DESC
                LIMIT 50
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            rs.getString("game_name"),
                            String.valueOf(rs.getInt("score")),
                            rs.getString("result"),
                            rs.getString("played")
                    });
                }
            }
        } catch (SQLException ex) {
            System.err.println("Could not load history: " + ex.getMessage());
        }
        return rows;
    }
}

package com.gamehub.scores;

import com.gamehub.db.DatabaseConnection;
import com.gamehub.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScoreService {
    public static final String DINO = "Blue Dino Runner";
    public static final String SNAKE = "Snake Game";
    public static final String TIC_TAC_TOE = "Tic Tac Toe";
    public static final String[] GAMES = {DINO, SNAKE, TIC_TAC_TOE};

    public void recordScore(User user, String gameName, int score, String result) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            saveHistory(connection, user.getUserId(), gameName, score, result);
            upsertScore(connection, user.getUserId(), gameName, score, result);
        } catch (SQLException ex) {
            System.err.println("Score not saved: " + ex.getMessage());
        }
    }

    public Map<String, Integer> highestScores(User user) {
        Map<String, Integer> scores = new LinkedHashMap<>();
        for (String game : GAMES) {
            scores.put(game, 0);
        }
        String sql = "SELECT game_name, highest_score FROM game_scores WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    scores.put(rs.getString("game_name"), rs.getInt("highest_score"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Could not load scores: " + ex.getMessage());
        }
        return scores;
    }

    public List<String[]> leaderboard(String gameFilter) {
        List<String[]> rows = new ArrayList<>();
        String sql = """
                SELECT u.username, s.game_name, s.highest_score, s.wins, s.losses, s.draws
                FROM game_scores s
                JOIN users u ON u.user_id = s.user_id
                WHERE (? = 'All Games' OR s.game_name = ?)
                ORDER BY s.highest_score DESC, s.wins DESC
                LIMIT 10
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, gameFilter);
            ps.setString(2, gameFilter);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            rs.getString("username"),
                            rs.getString("game_name"),
                            String.valueOf(rs.getInt("highest_score")),
                            String.valueOf(rs.getInt("wins")),
                            String.valueOf(rs.getInt("losses")),
                            String.valueOf(rs.getInt("draws"))
                    });
                }
            }
        } catch (SQLException ex) {
            System.err.println("Could not load leaderboard: " + ex.getMessage());
        }
        return rows;
    }

    private void saveHistory(Connection connection, int userId, String gameName, int score, String result) throws SQLException {
        String sql = "INSERT INTO game_history (user_id, game_name, score, result) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, gameName);
            ps.setInt(3, score);
            ps.setString(4, result);
            ps.executeUpdate();
        }
    }

    private void upsertScore(Connection connection, int userId, String gameName, int score, String result) throws SQLException {
        String sql = """
                INSERT INTO game_scores (user_id, game_name, highest_score, wins, losses, draws)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    highest_score = GREATEST(highest_score, VALUES(highest_score)),
                    wins = wins + VALUES(wins),
                    losses = losses + VALUES(losses),
                    draws = draws + VALUES(draws)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, gameName);
            ps.setInt(3, score);
            ps.setInt(4, "WIN".equals(result) ? 1 : 0);
            ps.setInt(5, "LOSS".equals(result) ? 1 : 0);
            ps.setInt(6, "DRAW".equals(result) ? 1 : 0);
            ps.executeUpdate();
        }
    }
}

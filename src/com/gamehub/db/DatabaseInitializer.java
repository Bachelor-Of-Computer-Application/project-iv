package com.gamehub.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        user_id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        full_name VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game_scores (
                        score_id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        game_name VARCHAR(60) NOT NULL,
                        highest_score INT NOT NULL DEFAULT 0,
                        wins INT NOT NULL DEFAULT 0,
                        losses INT NOT NULL DEFAULT 0,
                        draws INT NOT NULL DEFAULT 0,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        UNIQUE KEY uq_user_game (user_id, game_name),
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game_history (
                        history_id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        game_name VARCHAR(60) NOT NULL,
                        score INT NOT NULL DEFAULT 0,
                        result VARCHAR(20) NOT NULL DEFAULT 'PLAYED',
                        date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                    )
                    """);
        } catch (SQLException ex) {
            System.err.println("Database initialization skipped: " + ex.getMessage());
        }
    }
}

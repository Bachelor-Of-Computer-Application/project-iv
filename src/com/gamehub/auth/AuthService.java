package com.gamehub.auth;

import com.gamehub.db.DatabaseConnection;
import com.gamehub.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, full_name, password FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getString("password").equals(hash(password))) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("full_name"));
                }
            }
        }
        return null;
    }

    public void register(String username, String fullName, String password) throws SQLException {
        validate(username, password);
        String sql = "INSERT INTO users (username, full_name, password) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, fullName.trim());
            ps.setString(3, hash(password));
            ps.executeUpdate();
        }
    }

    private void validate(String username, String password) {
        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters.");
        }
        if (password == null || password.length() < 6 || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 6 characters and include a number.");
        }
    }

    private String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : bytes) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 unavailable", ex);
        }
    }
}

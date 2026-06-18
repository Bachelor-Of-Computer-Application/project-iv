CREATE DATABASE IF NOT EXISTS game_hub;
USE game_hub;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
    CONSTRAINT fk_scores_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS game_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    game_name VARCHAR(60) NOT NULL,
    score INT NOT NULL DEFAULT 0,
    result VARCHAR(20) NOT NULL DEFAULT 'PLAYED',
    date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO users (username, password, full_name)
VALUES (
    'demo',
    'e7c4525e34c940d02d0a49a7606175deec3c0cfc5ef122d12d70604c4f5e4c0a',
    'Demo Player'
)
ON DUPLICATE KEY UPDATE username = username;

-- Demo password: demo123

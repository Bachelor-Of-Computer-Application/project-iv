# Game Hub Project Documentation

## Objective

Game Hub is designed as a Java Swing and MySQL desktop application where users can access multiple games from one platform and keep persistent game records.

## Technology Stack

- Java
- Java Swing
- JDBC
- MySQL
- Object-Oriented Programming

## Architecture

The application is divided into clear modules:

- `auth`: registration, login, password validation, and hashing.
- `db`: JDBC configuration, connection creation, and table initialization.
- `model`: shared data objects.
- `ui`: login, dashboard, leaderboard, and reports screens.
- `games`: playable game panels.
- `scores`: score recording and leaderboard queries.
- `reports`: history and summary queries.

## Database Design

The system uses three main tables:

- `users`: stores account details and hashed passwords.
- `game_scores`: stores one high score/statistics record per user per game.
- `game_history`: stores every completed play session.

## Game Scoring

- Blue Dino Runner: score increases over time.
- Snake Game: score increases by 10 for each food eaten.
- Tic Tac Toe: win gives 100 points, draw gives 50 points, loss gives 0 points.

## Security Notes

Passwords are not stored as plain text. The application hashes passwords with SHA-256 before saving them. For a production system, salted hashing with BCrypt or Argon2 would be preferred.

## Future Enhancements

- Add more games.
- Add profile images and editable user profiles.
- Add difficulty settings.
- Add sound effects.
- Export reports to PDF or Excel.

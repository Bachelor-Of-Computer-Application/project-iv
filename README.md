# Game Hub

Game Hub is a Java Swing desktop application backed by MySQL. It gives players one dashboard to register, log in, launch games, track scores, view leaderboards, and review game history.

## Included Modules

- Dashboard Module: sidebar navigation, player profile, total games, high score summary, quick launch buttons.
- User Authentication Module: registration, login, logout, password validation, SHA-256 password hashing.
- Database Module: JDBC connection and MySQL schema initialization.
- Game Module: Blue Dino Runner, Snake Game, Tic Tac Toe.
- Score Management Module: high score updates and top 10 leaderboard.
- Report Module: history, total plays, highest score per game, most played game.

## Requirements

- Java JDK 17 or newer.
- MySQL Server.
- MySQL Connector/J `.jar` available on the classpath.

## Setup

1. Open MySQL and run `database/game_hub_schema.sql`.
2. Edit `src/com/gamehub/db/DatabaseConfig.java` if your MySQL username or password is different.
3. Compile with the MySQL Connector/J jar:

```bash
javac -cp "lib/mysql-connector-j.jar" -d out src/com/gamehub/**/*.java src/com/gamehub/*.java
```

4. Run:

```bash
java -cp "out;lib/mysql-connector-j.jar" com.gamehub.Main
```

On macOS/Linux, use `:` instead of `;` in the runtime classpath.

## Demo Account

- Username: `demo`
- Password: `demo123`

New users can also register from the login screen.

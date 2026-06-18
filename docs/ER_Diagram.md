# ER Diagram

```mermaid
erDiagram
    USERS ||--o{ GAME_SCORES : has
    USERS ||--o{ GAME_HISTORY : plays

    USERS {
        int user_id PK
        varchar username UK
        varchar password
        varchar full_name
        timestamp created_at
    }

    GAME_SCORES {
        int score_id PK
        int user_id FK
        varchar game_name
        int highest_score
        int wins
        int losses
        int draws
        timestamp updated_at
    }

    GAME_HISTORY {
        int history_id PK
        int user_id FK
        varchar game_name
        int score
        varchar result
        timestamp date_played
    }
```

## Relationships

- One user can have many score records.
- One user can have many history records.
- `game_scores` keeps one summary row per user and game.
- `game_history` stores every completed play session.

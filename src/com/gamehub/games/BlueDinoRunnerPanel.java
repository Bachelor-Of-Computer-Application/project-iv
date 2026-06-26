package com.gamehub.games;

import com.gamehub.model.User;
import com.gamehub.scores.ScoreService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BlueDinoRunnerPanel extends JPanel {
    private final User user;
    private final ScoreService scoreService;
    private final Timer timer;
    private int dinoY = 340;
    private int velocity = 0;
    private int obstacleX = 760;
    private int score = 0;
    private boolean running = true;
    private int obstacleSpeed;
    private int timerDelay;
    private int jumpPower;
    private final JLabel scoreLabel = new JLabel("Score: 0");
    private final JLabel levelLabel = new JLabel();
    private String currentLevel = "Medium";

    public BlueDinoRunnerPanel(User user, ScoreService scoreService) {
        this.user = user;
        this.scoreService = scoreService;
        String[] levels = {"Easy", "Medium", "Hard"};

        String level = (String) JOptionPane.showInputDialog(
                null,
                "Choose Difficulty",
                "Blue Dino Runner",
                JOptionPane.QUESTION_MESSAGE,
                null,
                levels,
                levels[0]);

        if (level == null) {
            level = "Medium";
        }

        currentLevel = level;

        switch (level) {
            case "Easy":
                obstacleSpeed = 5;
                timerDelay = 30;
                jumpPower = -18;
                break;

            case "Medium":
                obstacleSpeed = 8;
                timerDelay = 24;
                jumpPower = -18;
                break;

            case "Hard":
                obstacleSpeed = 12;
                timerDelay = 18;
                jumpPower = -18;
                break;
        }
        levelLabel.setText("Level: " + currentLevel);
        setLayout(null);
        setBackground(new Color(227, 244, 255));
        scoreLabel.setBounds(20, 15, 180, 30);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(scoreLabel);
        levelLabel.setBounds(220, 15, 180, 30);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        levelLabel.setText("Level: " + currentLevel);
        add(levelLabel);
        JButton restart = new JButton("Restart");
        restart.setBounds(680, 15, 100, 30);
        restart.addActionListener(e -> restart());
        add(restart);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && dinoY == 340 && running) {
                    velocity = jumpPower;
                }
            }
        });
        timer = new Timer(timerDelay, e -> update());;
        timer.start();
    }

    private void update() {
        if (!running) {
            repaint();
            return;
        }
        dinoY += velocity;
        velocity += 1;
        if (dinoY > 340) {
            dinoY = 340;
            velocity = 0;
        }
        obstacleX -= obstacleSpeed;
        if (obstacleX < -40) {
            obstacleX = 820;
        }
        score++;
        scoreLabel.setText("Score: " + score);
        if (obstacleX < 130 && obstacleX + 34 > 80 && dinoY + 54 > 345) {
            running = false;
            scoreService.recordScore(user, ScoreService.DINO, score, "PLAYED");
        }
        repaint();
    }

    private void restart() {
        dinoY = 340;
        velocity = 0;
        obstacleX = 760;
        score = 0;
        running = true;
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(68, 136, 210));
        g.fillRoundRect(80, dinoY, 50, 55, 10, 10);
        g.setColor(new Color(30, 45, 65));
        g.fillRect(0, 400, getWidth(), 6);
        g.setColor(new Color(230, 83, 83));
        g.fillRoundRect(obstacleX, 350, 34, 50, 8, 8);
        if (!running) {
            g.setColor(new Color(30, 45, 65));
            g.setFont(new Font("Segoe UI", Font.BOLD, 32));
            g.drawString("Game Over", 310, 230);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g.drawString("Press Restart to try again", 308, 260);
        }
    }
}

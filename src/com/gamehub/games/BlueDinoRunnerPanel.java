package com.gamehub.games;

import com.gamehub.model.User;
import com.gamehub.scores.ScoreService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
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
    private final JLabel scoreLabel = new JLabel("Score: 0");

    public BlueDinoRunnerPanel(User user, ScoreService scoreService) {
        this.user = user;
        this.scoreService = scoreService;
        setLayout(null);
        setBackground(new Color(227, 244, 255));
        scoreLabel.setBounds(20, 15, 180, 30);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(scoreLabel);
        JButton restart = new JButton("Restart");
        restart.setBounds(680, 15, 100, 30);
        restart.addActionListener(e -> restart());
        add(restart);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && dinoY == 340 && running) {
                    velocity = -18;
                }
            }
        });
        timer = new Timer(24, e -> update());
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
        obstacleX -= 8;
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

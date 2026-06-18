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
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGamePanel extends JPanel {
    private final User user;
    private final ScoreService scoreService;
    private final LinkedList<Point> snake = new LinkedList<>();
    private final Random random = new Random();
    private final Timer timer;
    private Point food = new Point(10, 10);
    private int dx = 1;
    private int dy = 0;
    private int score = 0;
    private boolean running = true;
    private final JLabel scoreLabel = new JLabel("Score: 0");

    public SnakeGamePanel(User user, ScoreService scoreService) {
        this.user = user;
        this.scoreService = scoreService;
        setLayout(null);
        setBackground(new Color(246, 249, 242));
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
                if (e.getKeyCode() == KeyEvent.VK_UP && dy != 1) { dx = 0; dy = -1; }
                if (e.getKeyCode() == KeyEvent.VK_DOWN && dy != -1) { dx = 0; dy = 1; }
                if (e.getKeyCode() == KeyEvent.VK_LEFT && dx != 1) { dx = -1; dy = 0; }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && dx != -1) { dx = 1; dy = 0; }
            }
        });
        restart();
        timer = new Timer(120, e -> update());
        timer.start();
    }

    private void update() {
        if (!running) {
            repaint();
            return;
        }
        Point head = snake.getFirst();
        Point next = new Point(head.x + dx, head.y + dy);
        if (next.x < 0 || next.y < 0 || next.x >= 30 || next.y >= 20 || snake.contains(next)) {
            running = false;
            scoreService.recordScore(user, ScoreService.SNAKE, score, "PLAYED");
            repaint();
            return;
        }
        snake.addFirst(next);
        if (next.equals(food)) {
            score += 10;
            scoreLabel.setText("Score: " + score);
            placeFood();
        } else {
            snake.removeLast();
        }
        repaint();
    }

    private void restart() {
        snake.clear();
        snake.add(new Point(6, 6));
        snake.add(new Point(5, 6));
        snake.add(new Point(4, 6));
        dx = 1;
        dy = 0;
        score = 0;
        running = true;
        placeFood();
        requestFocusInWindow();
    }

    private void placeFood() {
        do {
            food = new Point(random.nextInt(30), random.nextInt(20));
        } while (snake.contains(food));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = 22;
        int left = 70;
        int top = 70;
        g.setColor(new Color(210, 216, 222));
        g.drawRect(left, top, 30 * size, 20 * size);
        g.setColor(new Color(0, 150, 105));
        for (Point point : snake) {
            g.fillRoundRect(left + point.x * size, top + point.y * size, size - 2, size - 2, 6, 6);
        }
        g.setColor(new Color(220, 70, 70));
        g.fillOval(left + food.x * size, top + food.y * size, size - 2, size - 2);
        if (!running) {
            g.setColor(new Color(30, 45, 65));
            g.setFont(new Font("Segoe UI", Font.BOLD, 32));
            g.drawString("Game Over", 310, 300);
        }
    }
}

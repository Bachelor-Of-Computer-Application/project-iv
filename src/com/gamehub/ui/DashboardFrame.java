package com.gamehub.ui;

import com.gamehub.games.BlueDinoRunnerPanel;
import com.gamehub.games.SnakeGamePanel;
import com.gamehub.games.TicTacToePanel;
import com.gamehub.model.User;
import com.gamehub.scores.ScoreService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private final User user;
    private final ScoreService scoreService = new ScoreService();
    private final JPanel content = new JPanel(new CardLayout());
    private final JPanel home = new JPanel(new BorderLayout(16, 16));

    public DashboardFrame(User user) {
        this.user = user;
        setTitle("Game Hub - Dashboard");
        setSize(1040, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        add(sidebar(), BorderLayout.WEST);
        content.setBackground(Theme.BACKGROUND);
        content.add(home, "Dashboard");
        content.add(new LeaderboardPanel(), "Leaderboard");
        content.add(new ReportsPanel(user), "Reports");
        add(content, BorderLayout.CENTER);
        refreshHome();
    }

    private JPanel sidebar() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 0, 8));
        panel.setPreferredSize(new Dimension(210, 0));
        panel.setBackground(Theme.SIDEBAR);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JLabel logo = new JLabel("Game Hub");
        logo.setFont(Theme.TITLE);
        logo.setForeground(Color.WHITE);
        panel.add(logo);
        panel.add(nav("Dashboard", () -> show("Dashboard")));
        panel.add(nav("Leaderboard", () -> show("Leaderboard")));
        panel.add(nav("Reports", () -> show("Reports")));
        panel.add(nav("Blue Dino Runner", () -> openGame("Blue Dino Runner")));
        panel.add(nav("Snake Game", () -> openGame("Snake Game")));
        panel.add(nav("Tic Tac Toe", () -> openGame("Tic Tac Toe")));
        panel.add(nav("Logout", this::logout));
        return panel;
    }

    private JButton nav(String text, Runnable action) {
        JButton button = Theme.button(text);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setBackground(new Color(42, 52, 70));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void show(String name) {
        if ("Dashboard".equals(name)) {
            refreshHome();
        }
        ((CardLayout) content.getLayout()).show(content, name);
    }

    private void refreshHome() {
        home.removeAll();
        home.setBackground(Theme.BACKGROUND);
        home.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel top = Theme.card();
        top.setLayout(new GridLayout(1, 3, 14, 14));
        top.add(metric("Player", user.getFullName()));
        top.add(metric("Total Games", String.valueOf(ScoreService.GAMES.length)));
        top.add(metric("Most Recent Login", "Active now"));

        JPanel scores = Theme.card();
        scores.setLayout(new GridLayout(ScoreService.GAMES.length + 1, 1, 8, 8));
        scores.add(Theme.title("Highest Scores"));
        for (Map.Entry<String, Integer> entry : scoreService.highestScores(user).entrySet()) {
            scores.add(new JLabel(entry.getKey() + ": " + entry.getValue()));
        }

        JPanel quick = Theme.card();
        quick.setLayout(new GridLayout(1, 3, 12, 12));
        quick.add(gameButton("Blue Dino Runner"));
        quick.add(gameButton("Snake Game"));
        quick.add(gameButton("Tic Tac Toe"));

        home.add(Theme.title("Dashboard"), BorderLayout.NORTH);
        home.add(top, BorderLayout.CENTER);
        JPanel south = new JPanel(new GridLayout(2, 1, 16, 16));
        south.setOpaque(false);
        south.add(scores);
        south.add(quick);
        home.add(south, BorderLayout.SOUTH);
        home.revalidate();
        home.repaint();
    }

    private JPanel metric(String label, String value) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);
        JLabel name = new JLabel(label);
        name.setForeground(Color.GRAY);
        JLabel count = new JLabel(value);
        count.setFont(Theme.HEADING);
        panel.add(name);
        panel.add(count);
        return panel;
    }

    private JButton gameButton(String game) {
        JButton button = Theme.button(game);
        button.addActionListener(e -> openGame(game));
        return button;
    }

    private void openGame(String game) {
        JFrame frame = new JFrame(game);
        frame.setSize(820, 580);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if ("Blue Dino Runner".equals(game)) {
            frame.setContentPane(new BlueDinoRunnerPanel(user, scoreService));
        } else if ("Snake Game".equals(game)) {
            frame.setContentPane(new SnakeGamePanel(user, scoreService));
        } else {
            frame.setContentPane(new TicTacToePanel(user, scoreService));
        }
        frame.setVisible(true);
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}

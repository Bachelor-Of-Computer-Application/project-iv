package com.gamehub.ui;

import com.gamehub.scores.ScoreService;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class LeaderboardPanel extends JPanel {
    private final ScoreService scoreService = new ScoreService();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Player", "Game", "High Score", "Wins", "Losses", "Draws"}, 0);

    public LeaderboardPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BACKGROUND);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        JComboBox<String> filter = new JComboBox<>(new String[]{"All Games", ScoreService.DINO, ScoreService.SNAKE, ScoreService.TIC_TAC_TOE});
        filter.addActionListener(e -> load((String) filter.getSelectedItem()));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Theme.title("Leaderboard"), BorderLayout.WEST);
        top.add(filter, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        add(new JLabel("Top 10 scores are ranked by highest score and wins."), BorderLayout.SOUTH);
        load("All Games");
    }

    private void load(String game) {
        model.setRowCount(0);
        for (String[] row : scoreService.leaderboard(game)) {
            model.addRow(row);
        }
    }
}

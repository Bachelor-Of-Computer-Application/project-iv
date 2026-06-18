package com.gamehub.ui;

import com.gamehub.model.User;
import com.gamehub.reports.ReportService;
import com.gamehub.scores.ScoreService;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

public class ReportsPanel extends JPanel {
    private final ReportService reportService = new ReportService();
    private final ScoreService scoreService = new ScoreService();
    private final User user;
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Game", "Score", "Result", "Date Played"}, 0);

    public ReportsPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BACKGROUND);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        add(Theme.title("Reports"), BorderLayout.NORTH);
        add(summary(), BorderLayout.WEST);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        loadHistory();
    }

    private JPanel summary() {
        JPanel panel = Theme.card();
        panel.setLayout(new GridLayout(8, 1, 8, 8));
        panel.add(new javax.swing.JLabel("Total games played: " + reportService.totalGamesPlayed(user)));
        panel.add(new javax.swing.JLabel("Most played game: " + reportService.mostPlayedGame(user)));
        panel.add(new javax.swing.JLabel("Highest score per game"));
        for (Map.Entry<String, Integer> entry : scoreService.highestScores(user).entrySet()) {
            panel.add(new javax.swing.JLabel(entry.getKey() + ": " + entry.getValue()));
        }
        return panel;
    }

    private void loadHistory() {
        model.setRowCount(0);
        for (String[] row : reportService.history(user)) {
            model.addRow(row);
        }
    }
}

package com.gamehub.games;

import com.gamehub.model.User;
import com.gamehub.scores.ScoreService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class TicTacToePanel extends JPanel {
    private final User user;
    private final ScoreService scoreService;
    private final JButton[] cells = new JButton[9];
    private final JLabel status = new JLabel("Your turn: X");
    private boolean finished = false;

    public TicTacToePanel(User user, ScoreService scoreService) {
        this.user = user;
        this.scoreService = scoreService;
        setLayout(new BorderLayout(12, 12));
        status.setHorizontalAlignment(JLabel.CENTER);
        status.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(status, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(3, 3, 8, 8));
        for (int i = 0; i < cells.length; i++) {
            JButton cell = new JButton("");
            cell.setFont(new Font("Segoe UI", Font.BOLD, 58));
            int index = i;
            cell.addActionListener(e -> playerMove(index));
            cells[i] = cell;
            board.add(cell);
        }
        add(board, BorderLayout.CENTER);

        JButton restart = new JButton("New Game");
        restart.addActionListener(e -> reset());
        add(restart, BorderLayout.SOUTH);
    }

    private void playerMove(int index) {
        if (finished || !cells[index].getText().isEmpty()) {
            return;
        }
        cells[index].setText("X");
        if (finishIfNeeded()) {
            return;
        }
        computerMove();
        finishIfNeeded();
    }

    private void computerMove() {
        int move = bestComputerMove();
        if (move >= 0) {
            cells[move].setText("O");
        }
    }

    private int bestComputerMove() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText("O");
                boolean wins = "O".equals(winner());
                cells[i].setText("");
                if (wins) return i;
            }
        }
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText("X");
                boolean blocks = "X".equals(winner());
                cells[i].setText("");
                if (blocks) return i;
            }
        }
        int[] preference = {4, 0, 2, 6, 8, 1, 3, 5, 7};
        for (int index : preference) {
            if (cells[index].getText().isEmpty()) {
                return index;
            }
        }
        return -1;
    }

    private boolean finishIfNeeded() {
        String winner = winner();
        if (winner != null) {
            finished = true;
            String result = "X".equals(winner) ? "WIN" : "LOSS";
            int score = "WIN".equals(result) ? 100 : 0;
            scoreService.recordScore(user, ScoreService.TIC_TAC_TOE, score, result);
            status.setText("X".equals(winner) ? "You win!" : "Computer wins.");
            JOptionPane.showMessageDialog(this, status.getText());
            return true;
        }
        if (isDraw()) {
            finished = true;
            scoreService.recordScore(user, ScoreService.TIC_TAC_TOE, 50, "DRAW");
            status.setText("Draw game.");
            JOptionPane.showMessageDialog(this, "Draw game.");
            return true;
        }
        status.setText("Your turn: X");
        return false;
    }

    private String winner() {
        int[][] lines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : lines) {
            String a = cells[line[0]].getText();
            if (!a.isEmpty() && a.equals(cells[line[1]].getText()) && a.equals(cells[line[2]].getText())) {
                return a;
            }
        }
        return null;
    }

    private boolean isDraw() {
        for (JButton cell : cells) {
            if (cell.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void reset() {
        for (JButton cell : cells) {
            cell.setText("");
        }
        finished = false;
        status.setText("Your turn: X");
    }
}

package com.gamehub;

import com.gamehub.db.DatabaseInitializer;
import com.gamehub.ui.LoginFrame;
import com.gamehub.ui.Theme;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Theme.apply();
                DatabaseInitializer.initialize();
            } catch (Exception ex) {
                System.err.println("Startup warning: " + ex.getMessage());
            }
            new LoginFrame().setVisible(true);
        });
    }
}

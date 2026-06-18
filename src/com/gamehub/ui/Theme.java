package com.gamehub.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

public final class Theme {
    public static final Color BACKGROUND = new Color(245, 247, 251);
    public static final Color SIDEBAR = new Color(24, 31, 45);
    public static final Color PANEL = Color.WHITE;
    public static final Color PRIMARY = new Color(30, 112, 210);
    public static final Color ACCENT = new Color(0, 168, 150);
    public static final Color TEXT = new Color(35, 42, 54);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);

    private Theme() {
    }

    public static void apply() {
        UIManager.put("Button.font", BODY);
        UIManager.put("Label.font", BODY);
        UIManager.put("TextField.font", BODY);
        UIManager.put("PasswordField.font", BODY);
        UIManager.put("Table.font", BODY);
        UIManager.put("Table.rowHeight", 28);
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 231, 239)),
                new EmptyBorder(16, 16, 16, 16)));
        return panel;
    }

    public static JButton button(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        return button;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE);
        label.setForeground(TEXT);
        return label;
    }

    public static void setFonts(Component component) {
        component.setFont(BODY);
    }
}

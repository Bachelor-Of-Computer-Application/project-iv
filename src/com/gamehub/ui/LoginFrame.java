package com.gamehub.ui;

import com.gamehub.auth.AuthService;
import com.gamehub.model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();
    private final JTextField username = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);
    private final JTextField fullName = new JTextField(18);

    public LoginFrame() {
        setTitle("Game Hub - Login");
        setSize(430, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);

        JPanel form = Theme.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        form.add(Theme.title("Game Hub"), c);

        c.gridwidth = 1;
        addRow(form, c, 1, "Username", username);
        addRow(form, c, 2, "Full name", fullName);
        addRow(form, c, 3, "Password", password);

        JButton login = Theme.button("Login");
        JButton register = Theme.button("Register");
        register.setBackground(Theme.ACCENT);
        login.addActionListener(e -> login());
        register.addActionListener(e -> register());

        c.gridy = 4;
        c.gridx = 0;
        form.add(login, c);
        c.gridx = 1;
        form.add(register, c);

        add(form, BorderLayout.CENTER);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label, java.awt.Component field) {
        c.gridy = row;
        c.gridx = 0;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        form.add(field, c);
    }

    private void login() {
        try {
            User user = authService.login(username.getText(), String.valueOf(password.getPassword()));
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
                return;
            }
            new DashboardFrame(user).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void register() {
        try {
            authService.register(username.getText(), fullName.getText(), String.valueOf(password.getPassword()));
            JOptionPane.showMessageDialog(this, "Registration complete. You can log in now.");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}

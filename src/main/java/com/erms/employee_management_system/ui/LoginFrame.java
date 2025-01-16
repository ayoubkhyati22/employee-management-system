package com.erms.employee_management_system.ui;

import com.erms.employee_management_system.dto.AuthRequest;
import com.erms.employee_management_system.dto.AuthResponse;
import com.erms.employee_management_system.service.AuthService;
import com.erms.employee_management_system.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private final AuthService authService;
    private final EmployeeService employeeService;

    public LoginFrame(AuthService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;

        // Frame setup
        setTitle("Employee Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setResizable(false);

        // Main panel with MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 20", "[right][grow]", "[]20[][]20[]20[]"));

        // Title
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, "span 2, center, gapbottom 20, wrap");

        // Username
        mainPanel.add(new JLabel("Username:"), "right");
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, "growx, wrap");

        // Password
        mainPanel.add(new JLabel("Password:"), "right");
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, "growx, wrap");

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel, "span 2, center, wrap");

        // Login button
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[center, grow]"));
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        buttonPanel.add(loginButton, "w 100!");
        mainPanel.add(buttonPanel, "span 2, growx");

        // Add action listeners
        loginButton.addActionListener(e -> performLogin());

        // Set default button
        getRootPane().setDefaultButton(loginButton);

        // Add main panel
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(66, 139, 202));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void performLogin() {
        try {
            statusLabel.setText("");
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter username and password");
                return;
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Create auth request
            AuthRequest request = new AuthRequest();
            request.setUsername(username);
            request.setPassword(password);

            // Attempt authentication
            AuthResponse response = authService.authenticate(request);

            // Clear sensitive data
            passwordField.setText("");

            // Open main frame
            EventQueue.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(authService.getCurrentUser(), response.getToken(), employeeService, authService);
                mainFrame.setVisible(true);
                this.dispose();
            });

        } catch (Exception ex) {
            logger.error("Login failed", ex);
            statusLabel.setText("Login failed: " + ex.getMessage());
            passwordField.setText("");
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
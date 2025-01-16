package com.erms.employee_management_system.ui;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.service.EmployeeService;
import com.erms.employee_management_system.service.AuthService;
import com.erms.employee_management_system.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final UserPrincipal currentUser;
    private final EmployeeService employeeService;
    private final AuthService authService;
    private final String jwtToken;
    private final JTextField searchField;
    private final JComboBox<String> searchTypeCombo;

    public MainFrame(UserPrincipal currentUser, String jwtToken, EmployeeService employeeService, AuthService authService) {
        this.currentUser = currentUser;
        this.jwtToken = jwtToken;
        this.employeeService = employeeService;
        this.authService = authService;

        // Frame setup
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));

        // Initialize components
        searchField = new JTextField(20);
        searchTypeCombo = new JComboBox<>(new String[]{"Name", "Department", "Job Title"});

        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Add header panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createHeaderPanel(), gbc);

        // Add toolbar
        gbc.gridy = 1;
        mainPanel.add(createToolbar(), gbc);

        // Add table
        tableModel = createTableModel();
        employeeTable = new JTable(tableModel);
        setupTable();

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(employeeTable), gbc);

        // Add search panel
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(createSearchPanel(), gbc);

        // Add main panel to frame
        add(mainPanel);

        // Load data
        loadEmployeeData();

        // Display frame
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(new Color(66, 139, 202));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String roleName = currentUser.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("");

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + " (" + roleName + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        boolean canModify = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_HR") ||
                        auth.getAuthority().equals("ROLE_ADMIN"));

        if (canModify) {
            JButton addButton = new JButton("Add Employee");
            JButton editButton = new JButton("Edit Employee");
            JButton deleteButton = new JButton("Delete Employee");

            styleButton(addButton);
            styleButton(editButton);
            styleButton(deleteButton);

            addButton.addActionListener(e -> showEmployeeDialog(null));
            editButton.addActionListener(e -> editSelectedEmployee());
            deleteButton.addActionListener(e -> deleteSelectedEmployee());

            toolbar.add(addButton);
            toolbar.add(Box.createHorizontalStrut(5));
            toolbar.add(editButton);
            toolbar.add(Box.createHorizontalStrut(5));
            toolbar.add(deleteButton);
            toolbar.add(Box.createHorizontalStrut(10));
            toolbar.addSeparator();
            toolbar.add(Box.createHorizontalStrut(10));
        }

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadEmployeeData());
        toolbar.add(refreshButton);

        return toolbar;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private DefaultTableModel createTableModel() {
        String[] columnNames = {
                "ID", "Employee ID", "Full Name", "Job Title",
                "Department", "Status", "Contact", "Address"
        };

        return new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void setupTable() {
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setShowGrid(true);
        employeeTable.setGridColor(Color.LIGHT_GRAY);

        // Add double-click listener
        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editSelectedEmployee();
                }
            }
        });
    }

    private void loadEmployeeData() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            List<Employee> employees = employeeService.getAllEmployees();
            updateTableData(employees);
        } catch (Exception e) {
            showError("Error loading employees", e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void updateTableData(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getEmployeeId(),
                    employee.getFullName(),
                    employee.getJobTitle(),
                    employee.getDepartment(),
                    employee.getEmploymentStatus(),
                    employee.getContactInformation(),
                    employee.getAddress()
            });
        }
    }

    private void editSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                Employee employee = employeeService.getEmployeeById(id);
                showEmployeeDialog(employee);
            } catch (Exception e) {
                showError("Error loading employee", e);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    employeeService.deleteEmployee(id, currentUser);
                    loadEmployeeData();
                } catch (Exception e) {
                    showError("Error deleting employee", e);
                }
            }
        }
    }

    private void performSearch() {
        try {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                List<Employee> results = employeeService.searchEmployees(searchTerm);
                updateTableData(results);
            } else {
                loadEmployeeData();
            }
        } catch (Exception e) {
            showError("Error performing search", e);
        }
    }

    private void showEmployeeDialog(Employee employee) {
        EmployeeDialog dialog = new EmployeeDialog(this, employee, employeeService, currentUser);
        dialog.setVisible(true);
        if (dialog.isDataSaved()) {
            loadEmployeeData();
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            EventQueue.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame(authService, employeeService);
                loginFrame.setVisible(true);
            });
        }
    }

    private void showError(String message, Exception ex) {
        logger.error(message, ex);
        JOptionPane.showMessageDialog(this,
                message + ": " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(66, 139, 202));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 122, 183));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(66, 139, 202));
            }
        });
    }
}
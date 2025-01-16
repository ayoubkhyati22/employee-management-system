package com.erms.employee_management_system.ui;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.security.UserPrincipal;
import com.erms.employee_management_system.service.EmployeeService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeDialog extends JDialog {
    private final JTextField nameField = new JTextField(20);
    private final JTextField employeeIdField = new JTextField(10);
    private final JTextField jobTitleField = new JTextField(20);
    private final JComboBox<String> departmentCombo = new JComboBox<>(new String[]{"HR", "IT", "Finance", "Operations", "Sales"});
    private final JTextField hireDateField = new JTextField(10);
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "On Leave", "Terminated"});
    private final JTextField contactField = new JTextField(20);
    private final JTextArea addressArea = new JTextArea(3, 20);

    private final Employee employee;
    private final EmployeeService employeeService;
    private final UserPrincipal currentUser;
    private boolean dataSaved = false;

    public EmployeeDialog(Frame owner, Employee employee, EmployeeService employeeService, UserPrincipal currentUser) {
        super(owner, employee == null ? "Add New Employee" : "Edit Employee", true);
        this.employee = employee;
        this.employeeService = employeeService;
        this.currentUser = currentUser;

        setupUI();
        if (employee != null) {
            fillEmployeeData();
        }

        setResizable(false);
        pack();
        setLocationRelativeTo(owner);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configure text area
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);

        // Add form fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Employee ID:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(employeeIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Job Title:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(jobTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Department:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(departmentCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Hire Date:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(hireDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Status:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(statusCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Contact:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Address:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(addressScroll, gbc);

        // Add date format helper label
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("(Format: YYYY-MM-DD)"), gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);

        // Add action listeners
        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        // Add main panel to dialog
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);
    }

    private void fillEmployeeData() {
        nameField.setText(employee.getFullName());
        employeeIdField.setText(employee.getEmployeeId());
        jobTitleField.setText(employee.getJobTitle());
        departmentCombo.setSelectedItem(employee.getDepartment());
        hireDateField.setText(employee.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        statusCombo.setSelectedItem(employee.getEmploymentStatus());
        contactField.setText(employee.getContactInformation());
        addressArea.setText(employee.getAddress());

        // Disable employee ID field in edit mode
        employeeIdField.setEnabled(false);
    }

    private void saveEmployee() {
        try {
            // Validate required fields
            if (!validateFields()) {
                return;
            }

            // Create or update employee object
            Employee emp = employee == null ? new Employee() : employee;
            emp.setFullName(nameField.getText().trim());
            emp.setEmployeeId(employeeIdField.getText().trim());
            emp.setJobTitle(jobTitleField.getText().trim());
            emp.setDepartment((String) departmentCombo.getSelectedItem());
            emp.setHireDate(LocalDate.parse(hireDateField.getText().trim()));
            emp.setEmploymentStatus((String) statusCombo.getSelectedItem());
            emp.setContactInformation(contactField.getText().trim());
            emp.setAddress(addressArea.getText().trim());

            // Save to database
            if (employee == null) {
                employeeService.createEmployee(emp, currentUser);
            } else {
                employeeService.updateEmployee(emp.getId(), emp, currentUser);
            }

            dataSaved = true;
            dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use YYYY-MM-DD format.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving employee: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) errors.append("- Full Name is required\n");
        if (employeeIdField.getText().trim().isEmpty()) errors.append("- Employee ID is required\n");
        if (jobTitleField.getText().trim().isEmpty()) errors.append("- Job Title is required\n");
        if (hireDateField.getText().trim().isEmpty()) errors.append("- Hire Date is required\n");
        if (contactField.getText().trim().isEmpty()) errors.append("- Contact information is required\n");
        if (addressArea.getText().trim().isEmpty()) errors.append("- Address is required\n");

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Please fix the following errors:\n" + errors.toString(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isDataSaved() {
        return dataSaved;
    }
}
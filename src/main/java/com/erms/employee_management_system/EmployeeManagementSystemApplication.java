package com.erms.employee_management_system;

import com.erms.employee_management_system.service.EmployeeService;
import com.erms.employee_management_system.ui.LoginFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import com.erms.employee_management_system.service.AuthService;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class EmployeeManagementSystemApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(EmployeeManagementSystemApplication.class)
				.headless(false)
				.run(args);

		// Configure look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Launch the login frame
		SwingUtilities.invokeLater(() -> {
			LoginFrame loginFrame = context.getBean(LoginFrame.class);
			loginFrame.setVisible(true);
		});
	}
}
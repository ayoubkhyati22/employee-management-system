package com.erms.employee_management_system.config;

import com.erms.employee_management_system.ui.LoginFrame;
import com.erms.employee_management_system.service.AuthService;
import com.erms.employee_management_system.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SwingConfig {

    @Bean
    @Lazy
    public LoginFrame loginFrame(AuthService authService, EmployeeService employeeService) {
        return new LoginFrame(authService, employeeService);
    }
}
package com.erms.employee_management_system.config;

import com.erms.employee_management_system.model.Role;
import com.erms.employee_management_system.model.User;
import com.erms.employee_management_system.repository.RoleRepository;
import com.erms.employee_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializationConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            // Create roles if they don't exist
            Role adminRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(Role.RoleName.ROLE_ADMIN);
                        return roleRepository.save(role);
                    });

            Role hrRole = roleRepository.findByName(Role.RoleName.ROLE_HR)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(Role.RoleName.ROLE_HR);
                        return roleRepository.save(role);
                    });
            Role managerRole = roleRepository.findByName(Role.RoleName.ROLE_MANAGER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(Role.RoleName.ROLE_MANAGER);
                        return roleRepository.save(role);
                    });

            // Create admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@erms.com");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(hrRole);
                admin.setRoles(roles);
                userRepository.save(admin);
                System.out.println("Admin user created successfully!");
            }

            // Create HR user if it doesn't exist
            if (!userRepository.existsByUsername("hr")) {
                User hr = new User();
                hr.setUsername("hr");
                hr.setPassword(passwordEncoder.encode("hr123"));
                hr.setEmail("hr@erms.com");
                Set<Role> roles = new HashSet<>();
                roles.add(hrRole);
                hr.setRoles(roles);
                userRepository.save(hr);
                System.out.println("HR user created successfully!");
            }

            // Create MANAGER user if it doesn't exist
            if (!userRepository.existsByUsername("manager")) {
                User manager = new User();
                manager.setUsername("manager");
                manager.setPassword(passwordEncoder.encode("manager123"));
                manager.setEmail("manager@erms.com");
                Set<Role> roles = new HashSet<>();
                roles.add(managerRole);
                manager.setRoles(roles);
                userRepository.save(manager);
                System.out.println("MANAGER user created successfully!");
            }
        };
    }
}
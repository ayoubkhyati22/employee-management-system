package com.erms.employee_management_system.service;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.repository.EmployeeRepository;
import com.erms.employee_management_system.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditService auditService;

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Transactional
    public Employee createEmployee(Employee employee, UserPrincipal currentUser) {
        validateEmployeeData(employee);
        Employee savedEmployee = employeeRepository.save(employee);
        auditService.logAction(currentUser.getUsername(), "CREATE", "Created employee: " + employee.getEmployeeId());
        return savedEmployee;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Employee getEmployeeById(Long id) throws Exception {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new Exception("Employee not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Transactional
    public Employee updateEmployee(Long id, Employee employee, UserPrincipal currentUser) throws Exception {
        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setFullName(employee.getFullName());
        existingEmployee.setJobTitle(employee.getJobTitle());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setEmploymentStatus(employee.getEmploymentStatus());
        existingEmployee.setContactInformation(employee.getContactInformation());
        existingEmployee.setAddress(employee.getAddress());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        auditService.logAction(currentUser.getUsername(), "UPDATE", "Updated employee: " + employee.getEmployeeId());
        return updatedEmployee;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Transactional
    public void deleteEmployee(Long id, UserPrincipal currentUser) throws Exception {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        auditService.logAction(currentUser.getUsername(), "DELETE", "Deleted employee: " + employee.getEmployeeId());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<Employee> searchEmployees(String query) {
        return employeeRepository.findByFullNameContainingIgnoreCase(query);
    }

    private void validateEmployeeData(Employee employee) {
        if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
    }
}
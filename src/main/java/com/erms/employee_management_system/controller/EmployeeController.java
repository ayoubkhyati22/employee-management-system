package com.erms.employee_management_system.controller;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.service.EmployeeService;
import com.erms.employee_management_system.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "Endpoints for managing employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee record. Requires HR or ADMIN role.")
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Employee createdEmployee = employeeService.createEmployee(employee, currentUser);
        return ResponseEntity.ok(createdEmployee);
    }

    @Operation(summary = "Get an employee by ID", description = "Retrieves an employee by their ID. Available to HR, ADMIN, and MANAGERS.")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(summary = "Get all employees", description = "Retrieves all employees. Available to HR, ADMIN, and MANAGERS.")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee. Managers can only update employees in their department.")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee,
            @AuthenticationPrincipal UserPrincipal currentUser) throws Exception {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee, currentUser));
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee. Requires HR or ADMIN role.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) throws Exception {
        employeeService.deleteEmployee(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search employees", description = "Search employees by name, department, or job title.")
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String query) {
        return ResponseEntity.ok(employeeService.searchEmployees(query));
    }
}
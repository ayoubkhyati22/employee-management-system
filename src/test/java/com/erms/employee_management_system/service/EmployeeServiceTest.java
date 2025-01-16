package com.erms.employee_management_system.service;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.repository.EmployeeRepository;
import com.erms.employee_management_system.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;
    private UserPrincipal adminUser;
    private UserPrincipal hrUser;
    private UserPrincipal managerUser;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeId("EMP001");
        testEmployee.setFullName("John Doe");
        testEmployee.setJobTitle("Software Engineer");
        testEmployee.setDepartment("IT");
        testEmployee.setHireDate(LocalDate.now());
        testEmployee.setEmploymentStatus("Active");
        testEmployee.setContactInformation("john@example.com");
        testEmployee.setAddress("123 Main St");

        adminUser = UserPrincipal.builder()
                .id(1L)
                .username("admin")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        hrUser = UserPrincipal.builder()
                .id(2L)
                .username("hr")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_HR")))
                .build();

        managerUser = UserPrincipal.builder()
                .id(3L)
                .username("manager")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")))
                .department("IT")
                .build();
    }

    @Test
    void createEmployee_AsAdmin_Success() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);
        when(employeeRepository.existsByEmployeeId(anyString())).thenReturn(false);

        Employee created = employeeService.createEmployee(testEmployee, adminUser);

        assertNotNull(created);
        assertEquals(testEmployee.getEmployeeId(), created.getEmployeeId());
        verify(auditService).logAction(eq(adminUser.getUsername()), eq("CREATE"), anyString());
    }

    @Test
    void getEmployeeById_Success() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        Employee found = employeeService.getEmployeeById(1L);

        assertNotNull(found);
        assertEquals(testEmployee.getEmployeeId(), found.getEmployeeId());
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> found = employeeService.getAllEmployees();

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }

    @Test
    void updateEmployee_AsManager_SameDepartment_Success() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        testEmployee.setJobTitle("Senior Software Engineer");
        Employee updated = employeeService.updateEmployee(1L, testEmployee, managerUser);

        assertNotNull(updated);
        assertEquals("Senior Software Engineer", updated.getJobTitle());
        verify(auditService).logAction(eq(managerUser.getUsername()), eq("UPDATE"), anyString());
    }

    @Test
    void deleteEmployee_AsHR_Success() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeRepository).delete(testEmployee);

        employeeService.deleteEmployee(1L, hrUser);

        verify(employeeRepository).delete(testEmployee);
        verify(auditService).logAction(eq(hrUser.getUsername()), eq("DELETE"), anyString());
    }

    @Test
    void searchEmployees_Success() {
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findByFullNameContainingIgnoreCase("John")).thenReturn(employees);

        List<Employee> found = employeeService.searchEmployees("John");

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
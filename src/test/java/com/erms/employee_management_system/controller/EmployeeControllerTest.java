package com.erms.employee_management_system.controller;

import com.erms.employee_management_system.model.Employee;
import com.erms.employee_management_system.security.JwtTokenUtil;
import com.erms.employee_management_system.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Employee testEmployee;

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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_Success() throws Exception {
        when(employeeService.createEmployee(any(Employee.class), any()))
                .thenReturn(testEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(testEmployee.getEmployeeId()));
    }

    @Test
    @WithMockUser(roles = "HR")
    void getEmployee_Success() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEmployee.getId()));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getAllEmployees_Success() throws Exception {
        when(employeeService.getAllEmployees())
                .thenReturn(Arrays.asList(testEmployee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(testEmployee.getEmployeeId()));
    }

    @Test
    @WithMockUser(roles = "HR")
    void updateEmployee_Success() throws Exception {
        when(employeeService.updateEmployee(any(), any(Employee.class), any()))
                .thenReturn(testEmployee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEmployee.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_Success() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void searchEmployees_Success() throws Exception {
        when(employeeService.searchEmployees("John"))
                .thenReturn(Arrays.asList(testEmployee));

        mockMvc.perform(get("/api/employees/search")
                        .param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value(testEmployee.getFullName()));
    }
}
package com.erms.employee_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Column(nullable = false)
    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @Column(nullable = false)
    @NotBlank(message = "Department is required")
    private String department;

    @Column(name = "hire_date", nullable = false)
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @Column(name = "employment_status", nullable = false)
    @NotBlank(message = "Employment status is required")
    private String employmentStatus;

    @Column(name = "contact_info", nullable = false)
    @NotBlank(message = "Contact information is required")
    private String contactInformation;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    // Custom getters and setters if Lombok doesn't work
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
package com.erms.employee_management_system.repository;

import com.erms.employee_management_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(String department);

    List<Employee> findByEmploymentStatus(String status);

    List<Employee> findByFullNameContainingIgnoreCase(String name);

    List<Employee> findByEmployeeId(String employeeId);

    List<Employee> findByJobTitleContainingIgnoreCase(String jobTitle);

    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate")
    List<Employee> findByHireDateBetween(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    boolean existsByEmployeeId(String employeeId);
}
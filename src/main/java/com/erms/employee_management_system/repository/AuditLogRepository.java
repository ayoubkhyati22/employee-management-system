package com.erms.employee_management_system.repository;

import com.erms.employee_management_system.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUsername(String username);
    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> findByAction(String action);
}
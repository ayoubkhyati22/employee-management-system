package com.erms.employee_management_system.service;

import com.erms.employee_management_system.model.AuditLog;
import com.erms.employee_management_system.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String username, String action, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAuditLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }

    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}
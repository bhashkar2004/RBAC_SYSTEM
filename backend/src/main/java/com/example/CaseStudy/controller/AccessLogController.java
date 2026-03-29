package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.AccessLogDTO;
import com.example.CaseStudy.entity.AccessLogs;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class AccessLogController {

    @Autowired
    private AccessLogService accessLogService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessLogs>> getAllLogs() {
        return ResponseEntity.ok(accessLogService.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessLogs>> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accessLogService.getLogsByUser(userId));
    }

    @GetMapping("/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessLogs>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(accessLogService.getLogsByDateRange(from, to));
    }

    // GET all failed access logs - ADMIN or SECURITY
    @GetMapping("/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessLogs>> getFailedLogs() {
        return ResponseEntity.ok(accessLogService.getFailedLogs());
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<AccessLogs> createLog(@RequestBody AccessLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        AccessLogs log = accessLogService.logAccess(
                user, dto.getResource(), dto.getAction(), dto.getResult());
        return ResponseEntity.status(HttpStatus.CREATED).body(log);
    }
}

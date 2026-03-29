package com.example.CaseStudy.service;

import com.example.CaseStudy.entity.AccessLogs;
import com.example.CaseStudy.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogService {
    AccessLogs logAccess(User user, String resource, AccessLogs.Action action, AccessLogs.Result result);

    List<AccessLogs> getAllLogs();

    List<AccessLogs> getLogsByUser(Long userId);

    List<AccessLogs> getLogsByDateRange(LocalDateTime from, LocalDateTime to);

    List<AccessLogs> getFailedLogs();
}

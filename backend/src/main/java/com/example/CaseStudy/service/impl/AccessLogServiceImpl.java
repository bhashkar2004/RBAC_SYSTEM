package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.entity.AccessLogs;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.AccessLogRepository;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AccessLogs logAccess(User user, String resource,
            AccessLogs.Action action, AccessLogs.Result result) {
        AccessLogs log = new AccessLogs();
        log.setUser(user);
        log.setResource(resource);
        log.setAction(action);
        log.setResult(result);
        log.setTimeStamp(LocalDateTime.now());
        return accessLogRepository.save(log);
    }

    @Override
    public List<AccessLogs> getAllLogs() {
        return accessLogRepository.findAll();
    }

    @Override
    public List<AccessLogs> getLogsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return accessLogRepository.findByUserOrderByTimeStampDesc(user);
    }

    @Override
    public List<AccessLogs> getLogsByDateRange(LocalDateTime from, LocalDateTime to) {
        return accessLogRepository.findByTimeStampBetween(from, to);
    }

    @Override
    public List<AccessLogs> getFailedLogs() {
        return accessLogRepository.findByResult(AccessLogs.Result.FAILURE);
    }
}

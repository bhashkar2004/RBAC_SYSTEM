package com.example.CaseStudy.repository;

import com.example.CaseStudy.entity.AccessLogs;
import com.example.CaseStudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLogs, Long> {
    List<AccessLogs> findByUser(User user);

    List<AccessLogs> findByTimeStampBetween(LocalDateTime from, LocalDateTime to);

    List<AccessLogs> findByResult(AccessLogs.Result result);

    List<AccessLogs> findByUserOrderByTimeStampDesc(User user);

    void deleteByUser(User user);
}

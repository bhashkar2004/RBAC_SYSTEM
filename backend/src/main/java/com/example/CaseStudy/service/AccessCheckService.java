package com.example.CaseStudy.service;

import com.example.CaseStudy.entity.Permission;


public interface AccessCheckService {
    boolean hasPermission(Long userId, String resourceName, Permission.Action action);
}

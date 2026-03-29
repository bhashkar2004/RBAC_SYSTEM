package com.example.CaseStudy.service;

import com.example.CaseStudy.dto.PermissionDTO;
import com.example.CaseStudy.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();

    Permission getPermissionById(Long id);

    Permission createPermission(PermissionDTO dto);

    void deletePermission(Long id);
}

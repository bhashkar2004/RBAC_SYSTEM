package com.example.CaseStudy.service;

import com.example.CaseStudy.dto.RoleDTO;
import com.example.CaseStudy.entity.Roles;

import java.util.List;

public interface RoleService {
    List<Roles> getAllRoles();

    Roles getRoleById(Long id);

    Roles createRole(RoleDTO dto);

    Roles updateRole(Long id, RoleDTO dto);

    void deleteRole(Long id);

    Roles assignPermission(Long roleId, Long permissionId);

    Roles removePermission(Long roleId, Long permissionId);
}

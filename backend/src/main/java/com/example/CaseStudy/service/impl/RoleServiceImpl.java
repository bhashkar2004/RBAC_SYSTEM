package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.dto.RoleDTO;
import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.repository.PermissionRepository;
import com.example.CaseStudy.repository.RoleRepository;
import com.example.CaseStudy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public Roles createRole(RoleDTO dto) {
        if (roleRepository.existsByRoleName(dto.getRoleName())) {
            throw new RuntimeException("Role already exists: " + dto.getRoleName());
        }

        Roles role = new Roles();
        role.setRoleName(dto.getRoleName());
        role.setRoleDescription(dto.getRoleDescription());

        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(dto.getPermissionIds()));
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    @Override
    public Roles updateRole(Long id, RoleDTO dto) {
        Roles role = getRoleById(id);
        if (dto.getRoleName() != null)
            role.setRoleName(dto.getRoleName());
        if (dto.getRoleDescription() != null)
            role.setRoleDescription(dto.getRoleDescription());
        if (dto.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(dto.getPermissionIds()));
            role.setPermissions(permissions);
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Roles assignPermission(Long roleId, Long permissionId) {
        Roles role = getRoleById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }

    @Override
    public Roles removePermission(Long roleId, Long permissionId) {
        Roles role = getRoleById(roleId);
        role.getPermissions().removeIf(p -> p.getPermissionId().equals(permissionId));
        return roleRepository.save(role);
    }
}

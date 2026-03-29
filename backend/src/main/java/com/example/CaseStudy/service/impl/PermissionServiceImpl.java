package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.dto.PermissionDTO;
import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Resource;
import com.example.CaseStudy.repository.PermissionRepository;
import com.example.CaseStudy.repository.ResourceRepository;
import com.example.CaseStudy.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Override
    public Permission createPermission(PermissionDTO dto) {
        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + dto.getResourceId()));

        // Prevent duplicate action+resource combinations
        permissionRepository.findByActionAndResource(dto.getAction(), resource)
                .ifPresent(p -> {
                    throw new RuntimeException(
                            "Permission " + dto.getAction() + " on " + resource.getResourceName() + " already exists");
                });

        Permission permission = new Permission();
        permission.setAction(dto.getAction());
        permission.setResource(resource);

        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }
}

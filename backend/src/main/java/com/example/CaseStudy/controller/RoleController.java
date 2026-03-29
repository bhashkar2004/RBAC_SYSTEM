package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.RoleDTO;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'IT')")
    public ResponseEntity<List<Roles>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'IT')")
    public ResponseEntity<Roles> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public ResponseEntity<Roles> createRole(@RequestBody RoleDTO dto) {
        Roles created = roleService.createRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update role - ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public ResponseEntity<Roles> updateRole(@PathVariable Long id, @RequestBody RoleDTO dto) {
        return ResponseEntity.ok(roleService.updateRole(id, dto));
    }

    // DELETE role - ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
    }

    // POST assign a permission to a role - ADMIN only
    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public ResponseEntity<Roles> assignPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(roleService.assignPermission(roleId, permissionId));
    }

    // DELETE remove a permission from a role - ADMIN only
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public ResponseEntity<Roles> removePermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(roleService.removePermission(roleId, permissionId));
    }
}

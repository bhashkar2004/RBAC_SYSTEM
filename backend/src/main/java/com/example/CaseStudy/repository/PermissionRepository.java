package com.example.CaseStudy.repository;

import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByResource(Resource resource);

    Optional<Permission> findByActionAndResource(Permission.Action action, Resource resource);
}

package com.example.CaseStudy.repository;

import com.example.CaseStudy.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);
}

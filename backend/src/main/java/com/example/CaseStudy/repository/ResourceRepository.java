package com.example.CaseStudy.repository;

import com.example.CaseStudy.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByResourceName(String resourceName);

    boolean existsByResourceName(String resourceName);
}

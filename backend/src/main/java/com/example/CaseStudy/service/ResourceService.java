package com.example.CaseStudy.service;

import com.example.CaseStudy.dto.ResourceDTO;
import com.example.CaseStudy.entity.Resource;

import java.util.List;

public interface ResourceService {
    List<Resource> getAllResources();

    Resource getResourceById(Long id);

    Resource createResource(ResourceDTO dto);

    Resource updateResource(Long id, ResourceDTO dto);

    void deleteResource(Long id);
}

package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.dto.ResourceDTO;
import com.example.CaseStudy.entity.Resource;
import com.example.CaseStudy.repository.ResourceRepository;
import com.example.CaseStudy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }

    @Override
    public Resource createResource(ResourceDTO dto) {
        if (resourceRepository.existsByResourceName(dto.getResourceName())) {
            throw new RuntimeException("Resource already exists: " + dto.getResourceName());
        }
        Resource resource = new Resource();
        resource.setResourceName(dto.getResourceName());
        resource.setResourceDescription(dto.getResourceDescription());
        return resourceRepository.save(resource);
    }

    @Override
    public Resource updateResource(Long id, ResourceDTO dto) {
        Resource resource = getResourceById(id);
        if (dto.getResourceName() != null)
            resource.setResourceName(dto.getResourceName());
        if (dto.getResourceDescription() != null)
            resource.setResourceDescription(dto.getResourceDescription());
        return resourceRepository.save(resource);
    }

    @Override
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new RuntimeException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
}

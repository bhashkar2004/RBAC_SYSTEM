package com.example.CaseStudy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "RESOURCES")
public class Resource {

    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(name = "resource_name", nullable = false, unique = true)
    private String resourceName;

    @Column(name = "resource_description")
    private String resourceDescription;

    // Getters and Setters
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }
}

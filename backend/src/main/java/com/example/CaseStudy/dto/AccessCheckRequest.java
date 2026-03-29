package com.example.CaseStudy.dto;

import com.example.CaseStudy.entity.Permission;

/**
 * Request DTO for POST /api/access/check endpoint.
 * Checks whether a user has a specific action permission on a resource.
 */
public class AccessCheckRequest {

    private Long userId;
    private String resourceName;
    private Permission.Action action;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public Permission.Action getAction() { return action; }
    public void setAction(Permission.Action action) { this.action = action; }
}

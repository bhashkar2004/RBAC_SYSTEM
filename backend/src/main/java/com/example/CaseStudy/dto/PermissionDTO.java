package com.example.CaseStudy.dto;

import com.example.CaseStudy.entity.Permission;

public class PermissionDTO {

    private Long permissionId;
    private Permission.Action action;
    private Long resourceId;
    private String resourceName; 

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Permission.Action getAction() {
        return action;
    }

    public void setAction(Permission.Action action) {
        this.action = action;
    }

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
}

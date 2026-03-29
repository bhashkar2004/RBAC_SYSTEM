package com.example.CaseStudy.dto;

import com.example.CaseStudy.entity.AccessLogs;

public class AccessLogDTO {

    private Long userId;
    private String resource;
    private AccessLogs.Action action;
    private AccessLogs.Result result;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public AccessLogs.Action getAction() {
        return action;
    }

    public void setAction(AccessLogs.Action action) {
        this.action = action;
    }

    public AccessLogs.Result getResult() {
        return result;
    }

    public void setResult(AccessLogs.Result result) {
        this.result = result;
    }
}

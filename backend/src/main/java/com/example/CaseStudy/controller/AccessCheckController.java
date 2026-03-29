package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.AccessCheckRequest;
import com.example.CaseStudy.service.AccessCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/access")
public class AccessCheckController {

    @Autowired
    private AccessCheckService accessCheckService;

    @PostMapping("/check")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY')")
    public ResponseEntity<Map<String, Object>> checkAccess(@RequestBody AccessCheckRequest request) {

        boolean allowed = accessCheckService.hasPermission(
                request.getUserId(),
                request.getResourceName(),
                request.getAction());

        return ResponseEntity.ok(Map.of(
                "userId", request.getUserId(),
                "resource", request.getResourceName(),
                "action", request.getAction().name(),
                "allowed", allowed));
    }
}

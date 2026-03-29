package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.AccessRequestDTO;
import com.example.CaseStudy.service.AccessRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class AccessRequestController {

    @Autowired
    private AccessRequestService accessRequestService;

    @PostMapping
    public ResponseEntity<AccessRequestDTO> createRequest(@RequestBody Map<String, String> payload, Authentication auth) {
        String resource = payload.get("resource");
        String reason = payload.get("reason");
        return ResponseEntity.ok(accessRequestService.createRequest(auth.getName(), resource, reason));
    }

    @GetMapping("/me")
    public ResponseEntity<List<AccessRequestDTO>> getMyRequests(Authentication auth) {
        return ResponseEntity.ok(accessRequestService.getMyRequests(auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(accessRequestService.getAllRequests());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<List<AccessRequestDTO>> getPendingRequests() {
        return ResponseEntity.ok(accessRequestService.getPendingRequests());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<AccessRequestDTO> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(accessRequestService.approveRequest(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY', 'IT')")
    public ResponseEntity<AccessRequestDTO> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(accessRequestService.rejectRequest(id));
    }
}

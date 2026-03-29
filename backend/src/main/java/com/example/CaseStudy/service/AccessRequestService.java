package com.example.CaseStudy.service;

import com.example.CaseStudy.dto.AccessRequestDTO;
import java.util.List;

public interface AccessRequestService {
    AccessRequestDTO createRequest(String username, String resource, String reason);
    List<AccessRequestDTO> getMyRequests(String username);
    List<AccessRequestDTO> getAllRequests();
    List<AccessRequestDTO> getPendingRequests();
    AccessRequestDTO approveRequest(Long id);
    AccessRequestDTO rejectRequest(Long id);
}

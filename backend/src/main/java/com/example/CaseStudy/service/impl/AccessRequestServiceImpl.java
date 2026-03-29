package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.dto.AccessRequestDTO;
import com.example.CaseStudy.entity.AccessRequest;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.AccessRequestRepository;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.AccessRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessRequestServiceImpl implements AccessRequestService {

    @Autowired
    private AccessRequestRepository accessRequestRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public AccessRequestDTO createRequest(String username, String resource, String reason) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
                
        AccessRequest request = new AccessRequest();
        request.setUser(user);
        request.setResource(resource);
        request.setReason(reason);
        request.setStatus(AccessRequest.RequestStatus.PENDING);
        
        return AccessRequestDTO.from(accessRequestRepository.save(request));
    }

    @Override
    public List<AccessRequestDTO> getMyRequests(String username) {
        return accessRequestRepository.findAll().stream()
                .filter(r -> r.getUser().getUserName().equals(username))
                .filter(r -> r.getStatus() == AccessRequest.RequestStatus.APPROVED)
                .map(AccessRequestDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccessRequestDTO> getAllRequests() {
        return accessRequestRepository.findAll().stream()
                .map(AccessRequestDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccessRequestDTO> getPendingRequests() {
        return accessRequestRepository.findByStatus(AccessRequest.RequestStatus.PENDING).stream()
                .map(AccessRequestDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public AccessRequestDTO approveRequest(Long id) {
        AccessRequest request = accessRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        request.setStatus(AccessRequest.RequestStatus.APPROVED);
        return AccessRequestDTO.from(accessRequestRepository.save(request));
    }

    @Override
    public AccessRequestDTO rejectRequest(Long id) {
        AccessRequest request = accessRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        request.setStatus(AccessRequest.RequestStatus.REJECTED);
        return AccessRequestDTO.from(accessRequestRepository.save(request));
    }
}

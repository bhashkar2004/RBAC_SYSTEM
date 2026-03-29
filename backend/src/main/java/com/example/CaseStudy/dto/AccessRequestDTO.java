package com.example.CaseStudy.dto;

import com.example.CaseStudy.entity.AccessRequest;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccessRequestDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String resource;
    private String reason;
    private String status;
    private LocalDateTime requestDate;

    public static AccessRequestDTO from(AccessRequest entity) {
        AccessRequestDTO dto = new AccessRequestDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setUserName(entity.getUser().getUserName());
        dto.setUserEmail(entity.getUser().getUserEmail());
        dto.setResource(entity.getResource());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus().name());
        dto.setRequestDate(entity.getRequestDate());
        return dto;
    }
}

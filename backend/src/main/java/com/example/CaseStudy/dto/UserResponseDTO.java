package com.example.CaseStudy.dto;

import com.example.CaseStudy.entity.User;

import java.util.List;

/**
 * Safe response DTO — hides password and avoids circular reference from
 * the bidirectional User ↔ Roles relationship.
 */
public class UserResponseDTO {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userType;
    private String userStatus;
    private List<String> roles;

    
    public static UserResponseDTO from(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.userId = user.getUserId();
        dto.userName = user.getUserName();
        dto.userEmail = user.getUserEmail();
        dto.userType = user.getUserType() != null ? user.getUserType().name() : null;
        dto.userStatus = user.getUserStatus() != null ? user.getUserStatus().name() : null;
        dto.roles = user.getRoles().stream()
                .map(r -> r.getRoleName())
                .sorted()
                .toList();
        return dto;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getUserType() { return userType; }
    public String getUserStatus() { return userStatus; }
    public List<String> getRoles() { return roles; }
}

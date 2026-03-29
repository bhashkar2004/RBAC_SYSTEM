package com.example.CaseStudy.service;

import com.example.CaseStudy.dto.UserDTO;
import com.example.CaseStudy.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO createUser(UserDTO dto);

    UserResponseDTO updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);

    UserResponseDTO assignRole(Long userId, Long roleId);

    UserResponseDTO removeRole(Long userId, Long roleId);
}

package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.dto.UserDTO;
import com.example.CaseStudy.dto.UserResponseDTO;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.AccessLogRepository;
import com.example.CaseStudy.repository.RoleRepository;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllWithRoles().stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return UserResponseDTO.from(getUserEntityById(id));
    }

    @Override
    public UserResponseDTO createUser(UserDTO dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType() != null ? dto.getUserType() : User.UserType.EMPLOYEE);
        user.setUserStatus(dto.getUserStatus() != null ? dto.getUserStatus() : User.UserStatus.ACTIVE);

        if (dto.getRoleNames() != null && !dto.getRoleNames().isEmpty()) {
            Set<Roles> roles = new HashSet<>();
            for (String roleName : dto.getRoleNames()) {
                Roles role = roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return UserResponseDTO.from(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO dto) {
        User user = getUserEntityById(id);
        if (dto.getUserName() != null)
            user.setUserName(dto.getUserName());
        if (dto.getUserEmail() != null)
            user.setUserEmail(dto.getUserEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getUserType() != null)
            user.setUserType(dto.getUserType());
        if (dto.getUserStatus() != null)
            user.setUserStatus(dto.getUserStatus());
        if (dto.getRoleNames() != null) {
            Set<Roles> roles = new HashSet<>();
            for (String roleName : dto.getRoleNames()) {
                Roles role = roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        return UserResponseDTO.from(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        accessLogRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO assignRole(Long userId, Long roleId) {
        User user = getUserEntityById(userId);
        Roles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        user.getRoles().add(role);
        return UserResponseDTO.from(userRepository.save(user));
    }

    @Override
    public UserResponseDTO removeRole(Long userId, Long roleId) {
        User user = getUserEntityById(userId);
        user.getRoles().removeIf(r -> r.getRoleId().equals(roleId));
        return UserResponseDTO.from(userRepository.save(user));
    }
}

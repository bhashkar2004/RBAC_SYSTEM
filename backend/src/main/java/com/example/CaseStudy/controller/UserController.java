package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.UserDTO;
import com.example.CaseStudy.dto.UserResponseDTO;
import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication auth) {
        User user = userRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
        return ResponseEntity.ok(UserResponseDTO.from(user));
    }


    @GetMapping("/me/permissions")
    public ResponseEntity<List<Map<String, String>>> getCurrentUserPermissions(Authentication auth) {
        User user = userRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));

        List<Map<String, String>> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(p -> Map.of(
                        "resource", p.getResource().getResourceName(),
                        "action", p.getAction().name()))
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(permissions);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }


    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDTO> assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }


    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDTO> removeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }
}

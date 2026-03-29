package com.example.CaseStudy.controller;

import com.example.CaseStudy.dto.AuthRequest;
import com.example.CaseStudy.dto.AuthResponse;
import com.example.CaseStudy.entity.AccessLogs;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.security.JwtUtil;
import com.example.CaseStudy.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @org.springframework.web.bind.annotation.GetMapping("/test")
    public String test() {
        return "Backend is reachable";
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccessLogService accessLogService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            // Generate JWT token
            String token = jwtUtil.generateToken(request.getUsername());

            // Get user role for frontend redirection
            Optional<User> userOpt = userRepository.findByUserName(request.getUsername());
            String role = "EMPLOYEE";
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (!user.getRoles().isEmpty()) {
                    role = user.getRoles().iterator().next().getRoleName();
                }
                // Audit log — successful login
                accessLogService.logAccess(
                        user, "AUTH", AccessLogs.Action.LOGIN, AccessLogs.Result.SUCCESS);
            }

            return ResponseEntity.ok(new AuthResponse(token, role));

        } catch (BadCredentialsException e) {

            // Audit log — failed login attempt (best-effort; user may not exist)
            Optional<User> userOpt = userRepository.findByUserName(request.getUsername());
            userOpt.ifPresent(user -> accessLogService.logAccess(
                    user, "AUTH", AccessLogs.Action.LOGIN, AccessLogs.Result.FAILURE));

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }
}

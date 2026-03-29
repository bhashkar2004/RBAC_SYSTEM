package com.example.CaseStudy.service.impl;

import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.UserRepository;
import com.example.CaseStudy.service.AccessCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class AccessCheckServiceImpl implements AccessCheckService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean hasPermission(Long userId, String resourceName, Permission.Action action) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        for (Roles role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getAction() == action
                        && permission.getResource().getResourceName().equalsIgnoreCase(resourceName)) {
                    return true;
                }
            }
        }
        return false;
    }
}

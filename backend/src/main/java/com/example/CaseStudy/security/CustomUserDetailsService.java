package com.example.CaseStudy.security;

import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        @Transactional(readOnly = true)
        public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {

                User user = userRepository.findByUserName(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

                // Map each role to a Spring Security GrantedAuthority with ROLE_ prefix
                List<GrantedAuthority> authorities = user.getRoles()
                                .stream()
                                .map(Roles::getRoleName)
                                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                                .collect(Collectors.toList());

                return new org.springframework.security.core.userdetails.User(
                                user.getUserName(),
                                user.getPassword(),
                                authorities);
        }
}

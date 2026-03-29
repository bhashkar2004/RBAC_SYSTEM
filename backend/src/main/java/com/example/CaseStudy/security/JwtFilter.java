package com.example.CaseStudy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        logger.debug("JwtFilter: Request to: {} {}", request.getMethod(), request.getRequestURI());
        String header = request.getHeader("Authorization");

        try {
            String token = null;
            String username = null;

            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                username = jwtUtil.extractUsername(token);
                logger.debug("JwtFilter: Token for user: {}", username);
            }

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("JwtFilter: Authorities for user {}: {}", username, userDetails.getAuthorities());

            boolean isValid = jwtUtil.validateToken(token, userDetails.getUsername());
            logger.debug("JwtFilter: Token validation result for {}: {}", username, isValid);

            if (isValid) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug("JwtFilter: Authentication set in SecurityContext for {}", username);
            }
            }

        } catch (ExpiredJwtException e) {
            // Token expired — do not set authentication, let Security handle it
            logger.warn("JWT token has expired: " + e.getMessage());
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // Invalid token format — ignore and continue unauthenticated
            logger.warn("Invalid JWT token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}

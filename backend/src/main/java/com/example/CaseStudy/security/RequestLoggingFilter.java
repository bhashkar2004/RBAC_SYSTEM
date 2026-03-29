package com.example.CaseStudy.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("LOW-LEVEL-LOG: " + req.getMethod() + " " + req.getRequestURI());
        java.util.Collections.list(req.getHeaderNames()).forEach(headerName -> 
            System.out.println("  Header: " + headerName + " = " + req.getHeader(headerName))
        );

        chain.doFilter(request, response);
    }
}

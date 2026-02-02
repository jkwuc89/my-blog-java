package com.kwedinger.blog.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals("POST") && request.getRequestURI().equals("/session")) {
            System.out.println("=== LOGIN REQUEST ===");
            System.out.println("Method: " + request.getMethod());
            System.out.println("URI: " + request.getRequestURI());
            System.out.println("Username parameter: " + request.getParameter("username"));
            System.out.println("Password parameter: " + (request.getParameter("password") != null ? "***" : "null"));
            System.out.println("CSRF token: " + request.getParameter("_csrf"));
        }
        filterChain.doFilter(request, response);
    }
}

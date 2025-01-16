package com.erms.employee_management_system.service;

import com.erms.employee_management_system.dto.AuthRequest;
import com.erms.employee_management_system.dto.AuthResponse;
import com.erms.employee_management_system.security.JwtTokenUtil;
import com.erms.employee_management_system.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            logger.info("Attempting authentication for user: {}", request.getUsername());

            // Load user details
            UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(request.getUsername());

            // Create authentication token
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Set authentication in context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Authentication successful for user: {}", request.getUsername());

            // Generate JWT token
            String jwt = jwtTokenUtil.generateToken(userDetails);

            return AuthResponse.builder()
                    .token(jwt)
                    .username(userDetails.getUsername())
                    .build();

        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", request.getUsername(), e);
            throw e;
        }
    }

    public UserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return (UserPrincipal) auth.getPrincipal();
    }
}
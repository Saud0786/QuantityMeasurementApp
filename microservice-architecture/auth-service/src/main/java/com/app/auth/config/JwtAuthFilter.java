package com.app.auth.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.auth.model.UserEntity;
import com.app.auth.repository.UserRepository;
import com.app.auth.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/auth/login", "/auth/signup", "/oauth2", "/login/oauth2"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        
        // Skip auth for public paths
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        
        // Get token from cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Found token in cookie for path: " + path);
                    break;
                }
            }
        }
        
        // Also check Authorization header
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                System.out.println("Found token in Authorization header");
            }
        }

        if (token != null && jwtService.validateToken(token)) {
            try {
                String email = jwtService.getEmailFromToken(token);
                Long userId = jwtService.getUserIdFromToken(token);
                System.out.println("Email from token: " + email);
                System.out.println("User ID from token: " + userId);
                
                UserEntity user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, null);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    // ✅ Simple header setting - NO WRAPPER
                    response.setHeader("X-User-Id", String.valueOf(user.getId()));
                    response.setHeader("X-User-Email", user.getEmail());
                    
                    System.out.println("✅ Authentication set for: " + email + " (ID: " + user.getId() + ")");
                }
            } catch (Exception e) {
                System.out.println("JWT ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No valid token for path: " + path);
        }

        filterChain.doFilter(request, response);
    }
}
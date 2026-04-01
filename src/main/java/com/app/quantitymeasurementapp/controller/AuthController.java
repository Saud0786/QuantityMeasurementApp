package com.app.quantitymeasurementapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.dto.LoginRequest;
import com.app.quantitymeasurementapp.dto.SignupRequest;
import com.app.quantitymeasurementapp.dto.AuthResponse;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserEntity user = authService.signup(request);
        AuthResponse response = AuthResponse.builder()
                .success(true)
                .message("User registered successfully")
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        String token = authService.login(request, response);
        
        AuthResponse authResponse = AuthResponse.builder()
                .success(true)
                .message("Login successful")
                .token(token)
                .email(request.getEmail())
                .build();
        
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully");
        return ResponseEntity.ok(result);
    }
}
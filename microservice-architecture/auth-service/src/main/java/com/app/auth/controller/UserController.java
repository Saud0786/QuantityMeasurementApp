package com.app.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.auth.model.UserEntity;

import lombok.RequiredArgsConstructor;
import com.app.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserEntity user) {

        System.out.println("🔍 /api/auth/me called");
        System.out.println("User from @AuthenticationPrincipal: " + user);

        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId()); // ✅ ADDED USER ID
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            System.out.println("✅ Returning user: " + user.getEmail() + " with ID: " + user.getId());
            return ResponseEntity.ok(response);
        }

        System.out.println("❌ User is null - not authenticated");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not authenticated");
        return ResponseEntity.status(401).body(error);
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/account")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @AuthenticationPrincipal UserEntity user,
            jakarta.servlet.http.HttpServletResponse response) {
        
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Not authenticated");
            return ResponseEntity.status(401).body(error);
        }

        // Delete account
        authService.deleteAccount(user.getId());

        // Clear cookie
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Account deleted successfully");
        return ResponseEntity.ok(result);
    }
}
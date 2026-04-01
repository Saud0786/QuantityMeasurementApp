package com.app.quantitymeasurementapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.model.UserEntity;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        
        System.out.println("🔍 /api/auth/me called");
        System.out.println("User from @AuthenticationPrincipal: " + user);
        
        if (user != null) {
            Map<String, String> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            System.out.println("✅ Returning user: " + user.getEmail());
            return ResponseEntity.ok(response);
        }
        
        System.out.println("❌ User is null - not authenticated");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not authenticated");
        return ResponseEntity.status(401).body(error);
    }
}
package com.app.quantitymeasurementapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.model.LoginDTO;
import com.app.quantitymeasurementapp.model.SignupDTO;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@RequestBody SignupDTO signupDTO) {
        UserEntity user = authService.signup(signupDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletResponse response) {
        String token = authService.login(loginDTO, response);
        return ResponseEntity.ok(token);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(0); // ✅ delete cookie

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
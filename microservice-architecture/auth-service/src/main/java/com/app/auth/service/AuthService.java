package com.app.auth.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.auth.dto.LoginRequest;
import com.app.auth.dto.SignupRequest;
import com.app.auth.exception.UserAlreadyExistsException;
import com.app.auth.exception.UserNotFoundException;
import com.app.auth.exception.WrongPasswordException;
import com.app.auth.model.UserEntity;
import com.app.auth.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserEntity signup(SignupRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(UserEntity.Role.USER))
                .build();

        return userRepository.save(user);
    }

    public String login(LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail().trim().toLowerCase();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);

        return token;
    }

    public void deleteAccount(Long userId) {
        userRepository.deleteById(userId);
        System.out.println("✅ Account deleted for User ID: " + userId);
    }
}
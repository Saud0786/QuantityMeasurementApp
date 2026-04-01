package com.app.quantitymeasurementapp.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurementapp.dto.LoginRequest;
import com.app.quantitymeasurementapp.dto.SignupRequest;
import com.app.quantitymeasurementapp.exception.UserAlreadyExistsException;
import com.app.quantitymeasurementapp.exception.UserNotFoundException;
import com.app.quantitymeasurementapp.exception.WrongPasswordException;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.repository.UserRepository;

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
}
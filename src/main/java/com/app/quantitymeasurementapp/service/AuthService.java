package com.app.quantitymeasurementapp.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurementapp.model.LoginDTO;
import com.app.quantitymeasurementapp.model.SignupDTO;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.reposistory.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserEntity signup(SignupDTO signupDTO) {

        String email = signupDTO.getEmail().trim();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserEntity user = UserEntity.builder()
                .name(signupDTO.getName())
                .email(email)
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .roles(Set.of(UserEntity.Role.USER))
                .build();

        return userRepository.save(user);
    }

    public String login(LoginDTO loginDTO, HttpServletResponse response) {

        String email = loginDTO.getEmail().trim();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user);

        // FIXED COOKIE
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // IMPORTANT for localhost
        cookie.setPath("/");
        cookie.setMaxAge(86400);

        response.addCookie(cookie);

        return token;
    }
}
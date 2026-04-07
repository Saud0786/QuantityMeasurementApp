package com.app.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.auth.model.UserEntity;
import com.app.auth.repository.UserRepository;
import com.app.auth.service.JwtService;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        System.out.println("OAuth2 Success Handler STARTED");
        System.out.println("Request URL: " + request.getRequestURL());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        System.out.println("Email: " + email);
        System.out.println("Name: " + name);

        // Check if user exists
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    System.out.println("Creating new user...");
                    UserEntity newUser = UserEntity.builder()
                            .name(name)
                            .email(email)
                            .password("OAUTH_USER")
                            .roles(Set.of(UserEntity.Role.USER))
                            .build();
                    return userRepository.save(newUser);
                });

        System.out.println("User ID: " + user.getId());

        // Generate JWT
        String token = jwtService.generateToken(user);
        System.out.println("Token generated");

        // Set cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);

        // Set header
        response.setHeader("X-Auth-Token", token);
        response.setHeader("Access-Control-Expose-Headers", "X-Auth-Token");

        // ✅ FIX: Redirect to frontend with token in URL (for frontend to read)
        String redirectUrl = frontendUrl + "/dashboard?token=" + token + "&oauth_success=true";
        System.out.println("Redirecting to: " + redirectUrl);

        response.sendRedirect(redirectUrl);
    }
}
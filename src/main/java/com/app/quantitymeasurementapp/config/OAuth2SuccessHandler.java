package com.app.quantitymeasurementapp.config;

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

import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.repository.UserRepository;
import com.app.quantitymeasurementapp.service.JwtService;

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
        System.out.println("Token: " + token.substring(0, 50) + "...");

        // Set cookie - Make sure path and domain are correct
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // false for localhost
        cookie.setPath("/");
        cookie.setMaxAge(86400);  // 24 hours
        cookie.setDomain("localhost");  // Explicit domain
        response.addCookie(cookie);
        System.out.println("Cookie added: token=" + token.substring(0, 20) + "...");

        // Also set in response header for frontend to read
        response.setHeader("X-Auth-Token", token);

        // Redirect to frontend dashboard
        String redirectUrl = frontendUrl + "/dashboard?oauth_success=true";
        System.out.println("Redirecting to: " + redirectUrl);
        
        response.sendRedirect(redirectUrl);
    }
}
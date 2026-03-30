package com.app.quantitymeasurementapp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.reposistory.UserRepository;
import com.app.quantitymeasurementapp.service.JwtService;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Get user details from OAuth
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if user exists
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .name(name)
                            .email(email)
                            .password("OAUTH_USER") // dummy password
                            .roles(Set.of(UserEntity.Role.USER))
                            .build();

                    return userRepository.save(newUser);
                });

        // Generate JWT using UserEntity
        String token = jwtService.generateToken(user);
        // Save token in cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1 day

        response.addCookie(cookie);

        response.setContentType("text/html");

        response.getWriter().write("""
            <html>
                <head>
                    <title>Login Success</title>
                </head>
                <body style="font-family: Arial; text-align:center; margin-top:50px;">
                    <h2> Google Login Successful</h2>
                    <p><b>Your JWT Token:</b></p>
                    <textarea rows="5" cols="60">%s</textarea>
                    <br><br>
                    <p>Copy this token for API use.</p>
                </body>
            </html>
        """.formatted(token));
    }
}
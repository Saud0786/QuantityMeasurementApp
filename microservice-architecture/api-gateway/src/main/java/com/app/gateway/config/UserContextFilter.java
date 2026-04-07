package com.app.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserContextFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Extract token from Cookie
        String token = null;
        String cookieHeader = request.getHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            Pattern tokenPattern = Pattern.compile("token=([^;]+)");
            Matcher matcher = tokenPattern.matcher(cookieHeader);
            if (matcher.find()) {
                token = matcher.group(1);
            }
        }
        
        Long userId = null;
        String userEmail = null;
        
        if (token != null) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length > 1) {
                    String payload = new String(Base64.getDecoder().decode(parts[1]));
                    
                    Pattern subPattern = Pattern.compile("\"sub\":\"?(\\d+)\"?");
                    Matcher subMatcher = subPattern.matcher(payload);
                    if (subMatcher.find()) {
                        userId = Long.parseLong(subMatcher.group(1));
                    }
                    
                    Pattern emailPattern = Pattern.compile("\"email\":\"([^\"]+)\"");
                    Matcher emailMatcher = emailPattern.matcher(payload);
                    if (emailMatcher.find()) {
                        userEmail = emailMatcher.group(1);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error parsing JWT: " + e.getMessage());
            }
        }
        
        if (userId != null) {
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Email", userEmail != null ? userEmail : "unknown")
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }
        
        return chain.filter(exchange);
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}
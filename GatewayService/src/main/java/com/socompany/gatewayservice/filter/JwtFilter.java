package com.socompany.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");
            String path = request.getURI().getPath();

            // Skip request to /login and /register
            if (path.endsWith("/login") || path.endsWith("/register")) {
                logger.info("Skipping JWT Filter for: {}", path);
                return chain.filter(exchange);
            }

            if (request.getHeaders().containsKey("X-Internal-Call")) {
                logger.info("Skipping JWT filter for internal call: {}", path);
                return chain.filter(exchange);
            }

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info("Request doesn`t have JWT Token");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            try {
                logger.info("Token: {}", token);
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Username", claims.getSubject())
                        .header("X-Roles", claims.get("roles").toString())
                        .build();
                logger.info("Modified Request: {}", modifiedRequest.getURI());
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {}
}


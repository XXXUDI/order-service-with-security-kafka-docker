package com.socompany.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");
            String path = request.getURI().getPath();

            // Исключение для запросов к /api/users от SecurityService (внутренние вызовы)
            if (path.startsWith("/api/users") && !request.getHeaders().containsKey("X-Internal-Call")) {
                logger.info("Applying JWT filter to: {}", path);
            } else {
                logger.info("Skipping JWT filter for: {}", path);
                return chain.filter(exchange);
            }

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info("Request don`t have JWT Token");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            try {
                logger.info("Token: {}", token);
                Claims claims = Jwts.parser()
                        .setSigningKey("SecretKey".getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Roles", claims.get("roles").toString())
                        .build();
                logger.info("Modified Request: {}", modifiedRequest.getURI());
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
                return Mono.just(exchange.getResponse().setComplete()).then();
            }
        };
    }

    public static class Config {}
}



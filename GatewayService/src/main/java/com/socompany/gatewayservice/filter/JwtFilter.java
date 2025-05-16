package com.socompany.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");

            // If Request don`t have JWToken:
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Set response as UNAUTHORIZED
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Get JWT Token
            String token = authHeader.substring(7);
            try {
                // Parsing to get Claims (Roles)
                Claims claims = Jwts.parser()
                        .setSigningKey("SecretKey".getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Add roles to headers for microservices
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Roles", claims.get("roles").toString())
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                return Mono.just(exchange.getResponse().setComplete()).then();
            }
        };
    }

    public static class Config {}
}




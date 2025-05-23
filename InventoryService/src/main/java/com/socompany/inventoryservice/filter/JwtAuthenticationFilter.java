package com.socompany.inventoryservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Deprecated // This class is not usable.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.info("Request doesn`t have JWT Token");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // Extracting JWT token from request
        log.info("Extracted jwt token: {}", token);

        try {
            Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject(); // Extracting username from jwt
            String rolesStr = claims.get("roles").toString().replace("[", "").replace("]", "");
            List<String> roles = Arrays.asList(rolesStr.split(", ")).stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // Add 'ROLE_' if needed
                    .collect(Collectors.toList());

            log.info("Username: {}, Roles: {}", username, roles);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set for user: {} with authorities: {}", username, authorities);
            log.info("Security Context: {}", SecurityContextHolder.getContext().getAuthentication());
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token");
            return;
        }
        log.info("Request passed filter");
        filterChain.doFilter(request, response);
    }
}

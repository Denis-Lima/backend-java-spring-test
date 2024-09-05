package com.selaz.todoapp.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.selaz.todoapp.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TokenService {
    private final String issuer = "task-app";
    @Value("${jwt.secretKey}")
    private String secret;
    @Getter
    @Value("${jwt.expiration}")
    private Long expirationInMS;

    public static Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return Optional.empty();
        }

        String[] parts = authHeader.split("Bearer ");
        if (parts.length != 2) {
            return Optional.empty();
        }

        return Optional.of(parts[1]);
    }

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plus(expirationInMS, ChronoUnit.MILLIS).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }
}
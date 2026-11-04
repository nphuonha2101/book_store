package com.ecommerce.book_store.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    /**
     * Generate a production key with: <code>openssl rand -base64 48</code>.
     */
    @Value("${jwt.secret.key:}")
    private String secretKey;

    @Value("${jwt.expiration.time:86400000}")
    private long expirationTime;

    @PostConstruct
    public void validateConfiguration() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "jwt.secret.key is not configured. Set the JWT_SECRET environment variable.");
        }
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("jwt.secret.key must be a Base64-encoded value", e);
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "jwt.secret.key must contain at least 256 bits (32 bytes) for HS256");
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

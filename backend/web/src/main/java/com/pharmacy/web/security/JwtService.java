package com.pharmacy.web.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // WARNING: Ensure your secret key is at least 256 bits (32 characters/bytes) long.
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkeymysecretkey";

    // In 0.12.x, Keys.hmacShaKeyFor returns a SecretKey instance
    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) // Changed from setSubject()
                .issuedAt(new Date()) // Changed from setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // Changed from setExpiration()
                .signWith(key) // In 0.12.x, the algorithm (HS256) is automatically inferred from the key type
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser() // Changed from parserBuilder() to parser()
                .verifyWith(key) // Changed from setSigningKey()
                .build()
                .parseSignedClaims(token) // Changed from parseClaimsJws()
                .getPayload() // Changed from getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String email) {
        try {
            // 1. Verify the signature, expiration, and tampering
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            
            // 2. Extract the username and check if it matches the provided email
            String username = extractUsername(token);
            return username.equals(email);

        } catch (JwtException | IllegalArgumentException ex) {
            // If the token is expired, malformed, or has an invalid signature, catch it here
            return false;
        }
    }
}
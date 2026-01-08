package com.nikolaspc.jobapp.security;

import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.domain.UserRole;
import com.nikolaspc.jobapp.exception.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenProvider updated for JJWT 0.12.x.
 * Manages the lifecycle of JSON Web Tokens using secrets provided by Vault.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationInSeconds;

    /**
     * Extracts the signing key from the secret string retrieved via Vault.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return generateTokenFromUserDetails(user.getEmail(), user.getRole(), user.getId());
    }

    private String generateTokenFromUserDetails(String email, UserRole role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        claims.put("userId", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (jwtExpirationInSeconds * 1000));

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Returns the token expiration time in seconds for the AuthResponse.
     * (English comment as per user preference)
     */
    public long getTokenExpirationSeconds() {
        return this.jwtExpirationInSeconds;
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        String role = getClaims(token).get("role", String.class);
        return UserRole.valueOf(role);
    }

    public Long getUserIdFromToken(String token) {
        Object userId = getClaims(token).get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
            throw new JwtException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
            throw new JwtException("Expired JWT token");
        } catch (Exception ex) {
            log.error("Token validation failed: {}", ex.getMessage());
            throw new JwtException("Token validation failed");
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
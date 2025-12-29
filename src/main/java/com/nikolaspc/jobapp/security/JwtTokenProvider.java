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
 * JwtTokenProvider actualizado para JJWT 0.12.x
 * Implementa la lógica de generación, validación y extracción de claims.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:MyVerySecureSecretKeyForJWTThatMustBeAtLeast32CharactersLongForHS512AlgorithmSecurity}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400}")
    private long jwtExpirationMs;

    /**
     * Genera la llave de firma asegurando el encoding correcto.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera JWT token a partir de la entidad User.
     */
    public String generateToken(User user) {
        return generateTokenFromUserDetails(user.getEmail(), user.getRole(), user.getId());
    }

    /**
     * Crea el token con claims personalizados.
     */
    private String generateTokenFromUserDetails(String email, UserRole role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.toString());
        claims.put("userId", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs * 1000);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        String role = getClaims(token).get("role", String.class);
        return UserRole.valueOf(role);
    }

    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    /**
     * Valida la integridad y expiración del token.
     */
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
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
            throw new JwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
            throw new JwtException("JWT claims string is empty");
        }
    }

    /**
     * Extrae todos los claims del token.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retorna el tiempo de expiración configurado en segundos.
     * Requerido por AuthService para la construcción de AuthResponse.
     */
    public long getTokenExpirationSeconds() {
        return jwtExpirationMs;
    }
}
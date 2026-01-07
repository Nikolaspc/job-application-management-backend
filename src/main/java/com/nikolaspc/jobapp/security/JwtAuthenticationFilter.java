package com.nikolaspc.jobapp.security;

import com.nikolaspc.jobapp.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter
 * * Intercepts every incoming HTTP request to:
 * 1. Extract the token from the Authorization header.
 * 2. Validate token signature and expiration.
 * 3. Extract user metadata (email, role, ID).
 * 4. Populate the SecurityContextHolder for downstream authorization.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    // English: Constructor-based injection is preferred over @Autowired on fields
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                // English: Verify if the token is valid before proceeding
                if (jwtTokenProvider.validateToken(jwt)) {
                    String email = jwtTokenProvider.getEmailFromToken(jwt);
                    String role = jwtTokenProvider.getRoleFromToken(jwt).toString();
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                    // English: Create authentication token with ROLE_ prefix for Spring Security compatibility
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                    // English: Set custom details object to keep user metadata accessible in the context
                    authentication.setDetails(new JwtUserDetails(userId, email, role));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Successfully authenticated user: {} | Role: {}", email, role);
                }
            }
        } catch (JwtException ex) {
            // English: Log the error but continue the filter chain; EntryPoint will handle the 401
            log.error("Authentication failed: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the Authorization header.
     * Expected format: "Authorization: Bearer {token}"
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
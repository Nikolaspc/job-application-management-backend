package com.nikolaspc.jobapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point
 * Handles authentication exceptions and provides audit evidence for BSI/GDPR compliance.
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public JwtAuthenticationEntryPoint() {
        this.mapper = new ObjectMapper();
        // English: Register JavaTimeModule to handle LocalDateTime serialization correctly in the response body
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // English: Audit Evidence Generation (Requirement P0)
        // We log the IP, Path and Exception details internally to detect brute force or scanning.
        String remoteIp = request.getRemoteAddr();
        String path = request.getServletPath();

        log.warn("Unauthorized access attempt | IP: {} | Path: {} | Reason: {}",
                remoteIp, path, authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // English: Standardized JSON Response (OWASP Mitigation: No technical info leakage)
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        // English: Generic message for the client; technical details remain in internal logs
        body.put("message", "Full authentication is required to access this resource");
        body.put("path", path);

        mapper.writeValue(response.getOutputStream(), body);
    }
}
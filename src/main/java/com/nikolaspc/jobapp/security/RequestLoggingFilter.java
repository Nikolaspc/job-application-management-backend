package com.nikolaspc.jobapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Request Logging Filter
 * Implements Trace ID for request tracking and audit evidence (BSI Compliance).
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_LOG_VAR = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // English: Generate or retrieve a unique Trace ID for the request
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        // English: Add the ID to MDC (Mapped Diagnostic Context) for Logback/Logging system to use
        MDC.put(CORRELATION_ID_LOG_VAR, correlationId);

        // English: Return the ID in the response headers for debugging/support tickets
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        long startTime = System.currentTimeMillis();
        try {
            log.info("Incoming Request | Method: {} | Path: {} | IP: {}",
                    request.getMethod(), request.getServletPath(), request.getRemoteAddr());

            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Request Completed | Status: {} | Duration: {}ms",
                    response.getStatus(), duration);

            // English: Must clear MDC to avoid context contamination in thread pools
            MDC.clear();
        }
    }
}
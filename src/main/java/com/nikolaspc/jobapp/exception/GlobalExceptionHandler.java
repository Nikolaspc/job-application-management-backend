package com.nikolaspc.jobapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- SECURITY EXCEPTIONS (Requirement for BSI/GDPR Audit) ---

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        // English: Log as WARN for security monitoring (SIEM integration ready)
        log.warn("SECURITY - ACCESS DENIED: Path [{}], Method [{}], IP [{}]",
                request.getRequestURI(), request.getMethod(), request.getRemoteAddr());

        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access Denied - Insufficient Permissions", request, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {

        log.info("Auth Failure: Path [{}], Message [{}]", request.getRequestURI(), ex.getMessage());

        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed", request, null);
    }

    // --- BUSINESS EXCEPTIONS ---

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.debug("Validation failed for {}: {}", request.getRequestURI(), errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request, errors);
    }

    // --- GLOBAL FALLBACK ---

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        // English: Log full stack trace for internal review, but hide details from public client
        log.error("CRITICAL ERROR: [{}] at path [{}]", ex.getMessage(), request.getRequestURI(), ex);

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred. Technical ID: " + LocalDateTime.now().getNano(),
                request, null);
    }

    // --- HELPER ---

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, String message, HttpServletRequest request, Map<String, String> errors) {

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return new ResponseEntity<>(error, status);
    }
}
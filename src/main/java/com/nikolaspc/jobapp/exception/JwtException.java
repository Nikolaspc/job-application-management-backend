package com.nikolaspc.jobapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for JWT-related failures.
 * Marked with 401 Unauthorized status for global exception handling consistency.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
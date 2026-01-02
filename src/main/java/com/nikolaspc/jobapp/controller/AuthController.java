package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.AuthRequest;
import com.nikolaspc.jobapp.dto.AuthResponse;
import com.nikolaspc.jobapp.dto.RegisterRequest;
import com.nikolaspc.jobapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 *
 * Endpoints for:
 * - POST /api/auth/register - Register new user
 * - POST /api/auth/login - Login and obtain JWT token
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * Register new user
     *
     * @param request RegisterRequest with user data
     * @return AuthResponse with JWT token
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user",
            description = "Create new user account and receive JWT token")
    @ApiResponse(responseCode = "201",
            description = "User registered successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Invalid input or email already exists")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * User login
     *
     * @param request AuthRequest with email and password
     * @return AuthResponse with JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "Login user",
            description = "Authenticate user with email and password, receive JWT token")
    @ApiResponse(responseCode = "200",
            description = "Login successful",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401",
            description = "Invalid credentials")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login request received for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
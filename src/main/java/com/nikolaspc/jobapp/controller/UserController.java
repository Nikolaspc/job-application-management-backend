package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.UserDto;
import com.nikolaspc.jobapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    // English: This endpoint is protected. It requires a valid JWT in the Authorization header.
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        // English: Remove "Bearer " prefix if present
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        return authService.getUserFromToken(jwt)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }
}
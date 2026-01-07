package com.nikolaspc.jobapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom user details container for the Security Context.
 * Stores essential user metadata extracted from the JWT.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDetails {
    private Long id;
    private String email;
    private String role;
}
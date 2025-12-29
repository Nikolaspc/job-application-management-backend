package com.nikolaspc.jobapp.security;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Objeto personalizado que contiene información del JWT
 * Se usa para almacenar detalles en el Authentication object
 */
@Data
@AllArgsConstructor
public class JwtUserDetails {
    private Long userId;
    private String email;
    private String role;
}
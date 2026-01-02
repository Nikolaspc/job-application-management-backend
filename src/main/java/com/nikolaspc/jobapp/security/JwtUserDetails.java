package com.nikolaspc.jobapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDetails {
    private Long id;
    private String email;
    private String role;
}
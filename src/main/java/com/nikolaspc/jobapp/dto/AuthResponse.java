package com.nikolaspc.jobapp.dto;

import com.nikolaspc.jobapp.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;

    public static AuthResponse of(UserDto user, String token, Long expiresIn) {
        return AuthResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();
    }
}
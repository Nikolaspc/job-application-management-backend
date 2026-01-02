package com.nikolaspc.jobapp.dto;

import com.nikolaspc.jobapp.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat; // Importante
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 6) private String password;
    @Builder.Default private UserRole role = UserRole.CANDIDATE;

    @JsonFormat(pattern = "yyyy-MM-dd") // Asegura compatibilidad
    private LocalDate dateOfBirth;
}
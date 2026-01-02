package com.nikolaspc.jobapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

    private Long id; // Puede ser nulo al crear, pero el ID de la base de datos se asigna después

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Job Offer ID is required")
    private Long jobOfferId;

    @NotBlank(message = "Status is required")
    private String status;
}
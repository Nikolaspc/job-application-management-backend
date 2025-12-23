package com.nikolaspc.jobapp.dto.joboffer;

import jakarta.validation.constraints.NotBlank;

public record JobOfferRequestDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Employment type is required")
        String employmentType
) {}
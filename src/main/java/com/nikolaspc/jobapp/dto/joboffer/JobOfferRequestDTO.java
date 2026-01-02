package com.nikolaspc.jobapp.dto.joboffer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobOfferRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Employment type is required")
        String employmentType
) {}
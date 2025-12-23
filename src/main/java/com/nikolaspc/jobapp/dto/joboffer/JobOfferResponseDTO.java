package com.nikolaspc.jobapp.dto.joboffer;

import java.time.LocalDateTime;

public record JobOfferResponseDTO(
        Long id,
        String title,
        String description,
        String location,
        String employmentType,
        boolean active,
        LocalDateTime createdAt
) {}
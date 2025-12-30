package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para la entidad JobOffer.
 * Se utiliza CamelCase (Dto) para consistencia con el Service.
 */
@Mapper(componentModel = "spring")
public interface JobOfferMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "applications", ignore = true)
    JobOffer toEntity(JobOfferRequestDTO dto);

    // Cambiado a Dto (minúsculas) para coincidir con la llamada del Service
    JobOfferResponseDTO toResponseDto(JobOffer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "applications", ignore = true)
    void updateEntityFromDto(JobOfferRequestDTO dto, @MappingTarget JobOffer entity);
}
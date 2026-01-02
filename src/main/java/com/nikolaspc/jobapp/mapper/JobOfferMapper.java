package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobOfferMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "active", constant = "true") // <--- FIX: Ofertas nuevas nacen activas
    JobOffer toEntity(JobOfferRequestDTO dto);

    JobOfferResponseDTO toResponseDto(JobOffer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "active", ignore = true) // En update, no cambiamos el estado activo a menos que sea explícito (a futuro)
    void updateEntityFromDto(JobOfferRequestDTO dto, @MappingTarget JobOffer entity);
}
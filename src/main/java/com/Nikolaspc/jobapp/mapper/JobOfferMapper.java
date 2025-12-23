package com.Nikolaspc.jobapp.mapper;

import com.Nikolaspc.jobapp.domain.JobOffer;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobOfferMapper {

    JobOfferResponseDTO toResponseDto(JobOffer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applications", ignore = true)
    JobOffer toEntity(JobOfferRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDto(JobOfferRequestDTO dto, @MappingTarget JobOffer entity);
}
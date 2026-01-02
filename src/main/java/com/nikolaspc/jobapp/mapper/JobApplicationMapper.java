package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.JobApplication;
import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "jobOffer.id", target = "jobOfferId")
    JobApplicationDTO toDto(JobApplication entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "candidate", ignore = true) // Se asignará manualmente en el Service
    @Mapping(target = "jobOffer", ignore = true)   // Se asignará manualmente en el Service
    JobApplication toEntity(JobApplicationDTO dto);
}
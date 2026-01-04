package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.JobApplication;
import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobApplicationMapper {

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "jobOffer.id", target = "jobOfferId")
    JobApplicationDTO toDto(JobApplication entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "jobOffer", ignore = true)
    JobApplication toEntity(JobApplicationDTO dto);
}
package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateDTO toDto(Candidate candidate);

    @Mapping(target = "id", ignore = true)
    Candidate toEntity(CandidateDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CandidateDTO dto, @MappingTarget Candidate entity);
}
package com.Nikolaspc.jobapp.mapper;

import com.Nikolaspc.jobapp.domain.Candidate;
import com.Nikolaspc.jobapp.dto.CandidateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateDTO toDto(Candidate candidate);

    Candidate toEntity(CandidateDTO dto);
}

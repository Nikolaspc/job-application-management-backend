package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateDTO toDto(Candidate candidate);

    Candidate toEntity(CandidateDTO dto);
}

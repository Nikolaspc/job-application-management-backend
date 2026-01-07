package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CandidateMapper {

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    CandidateDTO toDto(Candidate candidate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "newCandidate", ignore = true) // English: Silences MapStruct warning
    Candidate toEntity(CandidateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "newCandidate", ignore = true) // English: Silences MapStruct warning
    void updateEntityFromDto(CandidateDTO dto, @MappingTarget Candidate entity);
}
package com.nikolaspc.jobapp.mapper;

import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class JobOfferMapperTest {

    private final JobOfferMapper mapper = Mappers.getMapper(JobOfferMapper.class);

    @Test
    void shouldMapEntityToResponseDto() {
        JobOffer entity = JobOffer.builder()
                .id(1L)
                .title("Backend Developer")
                .active(true)
                .build();

        JobOfferResponseDTO dto = mapper.toResponseDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.title()).isEqualTo("Backend Developer");
    }

    @Test
    void shouldMapRequestDtoToEntity_AndSetDefaults() {
        JobOfferRequestDTO request = new JobOfferRequestDTO(
                "Backend Developer",
                "Description",
                "Berlin",
                "FULL_TIME"
        );

        JobOffer entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        // Fixed: Using getActive() instead of isActive() for Boolean type
        assertThat(entity.getActive()).isTrue();
    }
}
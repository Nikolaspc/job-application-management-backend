package com.Nikolaspc.jobapp.mapper;

import com.Nikolaspc.jobapp.domain.JobOffer;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class JobOfferMapperTest {

    private final JobOfferMapper mapper = Mappers.getMapper(JobOfferMapper.class);

    @Test
    void shouldMapEntityToResponseDto() {
        // Usar el builder de la entidad JobOffer (que sí tiene @Builder de Lombok)
        JobOffer entity = JobOffer.builder()
                .id(1L)
                .title("Backend Developer")
                .description("Java + Spring Boot")
                .location("Berlin")
                .employmentType("FULL_TIME")
                .active(true)
                .build();

        JobOfferResponseDTO dto = mapper.toResponseDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Backend Developer");
        assertThat(dto.active()).isTrue();
    }

    @Test
    void shouldMapRequestDtoToEntity() {
        // Usar el constructor del record en lugar de builder
        JobOfferRequestDTO request = new JobOfferRequestDTO(
                "Backend Developer",
                "Java + Spring Boot",
                "Berlin",
                "FULL_TIME"
        );

        JobOffer entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo("Backend Developer");
        assertThat(entity.isActive()).isTrue();
    }
}
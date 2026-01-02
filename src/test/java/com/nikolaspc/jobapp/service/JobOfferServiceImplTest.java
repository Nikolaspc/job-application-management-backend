package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.JobOfferMapper;
import com.nikolaspc.jobapp.repository.JobOfferRepository;
import com.nikolaspc.jobapp.service.impl.JobOfferServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobOfferServiceImplTest {

    @Mock
    private JobOfferRepository repository;

    @Mock
    private JobOfferMapper mapper;

    @InjectMocks
    private JobOfferServiceImpl service;

    @Test
    @DisplayName("Should find job offer by id successfully")
    void findById_Success() {
        Long id = 1L;
        JobOffer offer = new JobOffer();
        offer.setId(id);
        JobOfferResponseDTO response = new JobOfferResponseDTO(
                id,
                "Java Dev",
                "Desc",
                "Berlin",
                "FULL_TIME",
                true,
                LocalDateTime.now()
        );

        given(repository.findById(id)).willReturn(Optional.of(offer));
        given(mapper.toResponseDto(offer)).willReturn(response);

        JobOfferResponseDTO result = service.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when finding non-existent job offer")
    void findById_WhenNotExists_ShouldThrowException() {
        Long id = 999L;
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should create job offer successfully")
    void create_Success() {
        JobOfferRequestDTO request = new JobOfferRequestDTO("Java Dev", "Desc", "Berlin", "FULL_TIME");
        JobOffer offer = new JobOffer();
        offer.setTitle("Java Dev");
        JobOffer savedOffer = new JobOffer();
        savedOffer.setId(10L);
        JobOfferResponseDTO response = new JobOfferResponseDTO(
                10L,
                "Java Dev",
                "Desc",
                "Berlin",
                "FULL_TIME",
                true,
                LocalDateTime.now()
        );

        given(mapper.toEntity(request)).willReturn(offer);
        given(repository.save(any(JobOffer.class))).willReturn(savedOffer);
        given(mapper.toResponseDto(savedOffer)).willReturn(response);

        JobOfferResponseDTO result = service.create(request);

        assertThat(result.id()).isEqualTo(10L);
        verify(repository).save(any(JobOffer.class));
    }

    @Test
    @DisplayName("Should delete job offer when it exists")
    void delete_Success() {
        Long id = 1L;
        given(repository.existsById(id)).willReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent job offer")
    void delete_WhenNotExists_ShouldThrowException() {
        Long id = 999L;
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).deleteById(anyLong());
    }
}
package com.Nikolaspc.jobapp.service;

import com.Nikolaspc.jobapp.domain.JobOffer;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.Nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.Nikolaspc.jobapp.mapper.JobOfferMapper;
import com.Nikolaspc.jobapp.repository.JobOfferRepository;
import com.Nikolaspc.jobapp.service.impl.JobOfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobOfferServiceImplTest {

    @Mock
    private JobOfferRepository repository;

    @Mock
    private JobOfferMapper mapper;

    @InjectMocks
    private JobOfferServiceImpl service;

    private JobOffer jobOffer;

    @BeforeEach
    void setUp() {
        jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Java Dev");
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Offer not found with id: " + id);
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        Long id = 999L;
        // SOLUCIÓN TÉCNICA: Usamos un mock del DTO para evitar conflictos de tipos en el constructor
        JobOfferRequestDTO dto = mock(JobOfferRequestDTO.class);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Offer not found with id: " + id);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        Long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Offer not found with id: " + id);
    }
}
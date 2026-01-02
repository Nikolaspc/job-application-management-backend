package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.exception.BadRequestException;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.CandidateMapper;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.service.impl.CandidateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidateService Unit Tests")
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository repository;

    @Mock
    private CandidateMapper mapper;

    @InjectMocks
    private CandidateServiceImpl service;

    private Candidate candidate;
    private CandidateDTO candidateDTO;

    @BeforeEach
    void setUp() {
        candidate = Candidate.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        candidateDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();
    }

    @Test
    @DisplayName("Should create (save) candidate successfully")
    void save_WithValidData_ShouldReturnCreatedCandidate() {
        when(mapper.toEntity(candidateDTO)).thenReturn(candidate);
        when(repository.save(candidate)).thenReturn(candidate);
        when(mapper.toDto(candidate)).thenReturn(candidateDTO);

        // Fixed: Call save() instead of create()
        CandidateDTO result = service.save(candidateDTO);

        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void save_WithDuplicateEmail_ShouldThrowException() {
        when(mapper.toEntity(candidateDTO)).thenReturn(candidate);
        when(repository.save(any(Candidate.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // Fixed: Call save() instead of create()
        assertThatThrownBy(() -> service.save(candidateDTO))
                .isInstanceOf(BadRequestException.class);
    }
}
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

/**
 * Unit tests for CandidateServiceImpl.
 * Tests all business logic including:
 * - CRUD operations
 * - Age validation (18+ requirement)
 * - Email uniqueness constraint
 * - Exception handling
 */
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
        // Setup test data
        candidate = Candidate.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max.mustermann@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        candidateDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max.mustermann@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();
    }

    @Test
    @DisplayName("Should return all candidates successfully")
    void findAll_ShouldReturnAllCandidates() {
        // Arrange
        List<Candidate> candidates = Arrays.asList(candidate);
        when(repository.findAll()).thenReturn(candidates);
        when(mapper.toDto(any(Candidate.class))).thenReturn(candidateDTO);

        // Act
        List<CandidateDTO> result = service.findAll();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("max.mustermann@example.com");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no candidates exist")
    void findAll_WhenNoCandidates_ShouldReturnEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<CandidateDTO> result = service.findAll();

        // Assert
        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find candidate by ID successfully")
    void findById_WhenExists_ShouldReturnCandidate() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(candidate));
        when(mapper.toDto(candidate)).thenReturn(candidateDTO);

        // Act
        CandidateDTO result = service.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("max.mustermann@example.com");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when candidate not found")
    void findById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Candidate")
                .hasMessageContaining("999");

        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should create candidate successfully")
    void create_WithValidData_ShouldReturnCreatedCandidate() {
        // Arrange
        when(mapper.toEntity(candidateDTO)).thenReturn(candidate);
        when(repository.save(candidate)).thenReturn(candidate);
        when(mapper.toDto(candidate)).thenReturn(candidateDTO);

        // Act
        CandidateDTO result = service.create(candidateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("max.mustermann@example.com");
        verify(repository, times(1)).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void create_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        when(mapper.toEntity(candidateDTO)).thenReturn(candidate);
        when(repository.save(any(Candidate.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // Act & Assert
        assertThatThrownBy(() -> service.create(candidateDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists")
                .hasMessageContaining(candidateDTO.getEmail());

        verify(repository, times(1)).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should reject candidate under 18 years old")
    void create_WithUnderageCandidate_ShouldThrowException() {
        // Arrange - Create a candidate who is 17 years old
        LocalDate underageDate = LocalDate.now().minusYears(17);
        CandidateDTO underageDTO = CandidateDTO.builder()
                .firstName("Young")
                .lastName("Candidate")
                .email("young@example.com")
                .dateOfBirth(underageDate)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> service.create(underageDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("18 years old");

        verify(repository, never()).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should accept candidate exactly 18 years old")
    void create_WithCandidateExactly18_ShouldSucceed() {
        // Arrange - Create a candidate who turned 18 today
        LocalDate exactlyEighteenDate = LocalDate.now().minusYears(18);
        CandidateDTO validDTO = CandidateDTO.builder()
                .firstName("Valid")
                .lastName("Candidate")
                .email("valid@example.com")
                .dateOfBirth(exactlyEighteenDate)
                .build();

        Candidate validCandidate = Candidate.builder()
                .firstName("Valid")
                .lastName("Candidate")
                .email("valid@example.com")
                .dateOfBirth(exactlyEighteenDate)
                .build();

        when(mapper.toEntity(validDTO)).thenReturn(validCandidate);
        when(repository.save(validCandidate)).thenReturn(validCandidate);
        when(mapper.toDto(validCandidate)).thenReturn(validDTO);

        // Act
        CandidateDTO result = service.create(validDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should allow candidate with null birth date")
    void create_WithNullBirthDate_ShouldSucceed() {
        // Arrange
        CandidateDTO dtoWithoutBirthDate = CandidateDTO.builder()
                .firstName("No")
                .lastName("Birthday")
                .email("nobday@example.com")
                .dateOfBirth(null)
                .build();

        Candidate candidateWithoutBirthDate = Candidate.builder()
                .firstName("No")
                .lastName("Birthday")
                .email("nobday@example.com")
                .dateOfBirth(null)
                .build();

        when(mapper.toEntity(dtoWithoutBirthDate)).thenReturn(candidateWithoutBirthDate);
        when(repository.save(candidateWithoutBirthDate)).thenReturn(candidateWithoutBirthDate);
        when(mapper.toDto(candidateWithoutBirthDate)).thenReturn(dtoWithoutBirthDate);

        // Act
        CandidateDTO result = service.create(dtoWithoutBirthDate);

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(Candidate.class));
    }
}
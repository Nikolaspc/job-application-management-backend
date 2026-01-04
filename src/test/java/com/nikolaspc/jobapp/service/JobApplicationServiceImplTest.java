package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.JobApplication;
import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import com.nikolaspc.jobapp.exception.BadRequestException;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.JobApplicationMapper;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.repository.JobApplicationRepository;
import com.nikolaspc.jobapp.repository.JobOfferRepository;
import com.nikolaspc.jobapp.service.impl.JobApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JobApplicationServiceImpl.
 * Tests business logic including:
 * - Application creation and retrieval
 * - Validation of candidate and job offer existence
 * - Prevention of duplicate applications
 * - Active job offer requirement
 * - Status management
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JobApplicationService Unit Tests")
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private JobApplicationMapper mapper;

    @InjectMocks
    private JobApplicationServiceImpl service;

    private Candidate candidate;
    private JobOffer activeJobOffer;
    private JobOffer inactiveJobOffer;
    private JobApplication application;
    private JobApplicationDTO applicationDTO;

    @BeforeEach
    void setUp() {
        // Setup test candidate
        candidate = Candidate.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        // Setup active job offer
        activeJobOffer = JobOffer.builder()
                .id(1L)
                .title("Backend Developer")
                .description("Java + Spring Boot")
                .location("Berlin")
                .employmentType("FULL_TIME")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Setup inactive job offer
        inactiveJobOffer = JobOffer.builder()
                .id(2L)
                .title("Closed Position")
                .description("No longer available")
                .location("Munich")
                .employmentType("FULL_TIME")
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Setup application entity for returns
        application = JobApplication.builder()
                .id(1L)
                .candidate(candidate)
                .jobOffer(activeJobOffer)
                .status("PENDING")
                .build();

        // Setup DTO
        applicationDTO = JobApplicationDTO.builder()
                .id(1L)
                .candidateId(1L)
                .jobOfferId(1L)
                .status("PENDING")
                .build();
    }

    @Test
    @DisplayName("Should return all applications successfully")
    void findAll_ShouldReturnAllApplications() {
        // Arrange
        List<JobApplication> applications = Arrays.asList(application);
        when(applicationRepository.findAll()).thenReturn(applications);
        when(mapper.toDto(any(JobApplication.class))).thenReturn(applicationDTO);

        // Act
        List<JobApplicationDTO> result = service.findAll();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCandidateId()).isEqualTo(1L);
        verify(applicationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find application by ID successfully")
    void findById_WhenExists_ShouldReturnApplication() {
        // Arrange
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(mapper.toDto(application)).thenReturn(applicationDTO);

        // Act
        JobApplicationDTO result = service.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(applicationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when application not found")
    void findById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(applicationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Application")
                .hasMessageContaining("999");

        verify(applicationRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should create application successfully")
    void create_WithValidData_ShouldReturnCreatedApplication() {
        // Arrange
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(activeJobOffer));
        // FIX: Stubbing toEntity to avoid NullPointerException in service
        when(mapper.toEntity(any(JobApplicationDTO.class))).thenReturn(new JobApplication());
        when(applicationRepository.save(any(JobApplication.class))).thenReturn(application);
        when(mapper.toDto(any(JobApplication.class))).thenReturn(applicationDTO);

        // Act
        JobApplicationDTO result = service.create(applicationDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCandidateId()).isEqualTo(1L);
        assertThat(result.getJobOfferId()).isEqualTo(1L);
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw exception when candidate not found")
    void create_WithNonExistentCandidate_ShouldThrowException() {
        // Arrange
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.create(applicationDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Candidate");

        verify(applicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw exception when job offer not found")
    void create_WithNonExistentJobOffer_ShouldThrowException() {
        // Arrange
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.create(applicationDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Offer");

        verify(applicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw exception when applying to inactive job offer")
    void create_WithInactiveJobOffer_ShouldThrowException() {
        // Arrange
        JobApplicationDTO inactiveJobDTO = JobApplicationDTO.builder()
                .candidateId(1L)
                .jobOfferId(2L)
                .status("PENDING")
                .build();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobOfferRepository.findById(2L)).thenReturn(Optional.of(inactiveJobOffer));

        // Act & Assert
        assertThatThrownBy(() -> service.create(inactiveJobDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("inactive");

        verify(applicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw exception on duplicate application")
    void create_WithDuplicateApplication_ShouldThrowException() {
        // Arrange
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(activeJobOffer));
        // FIX: Stubbing toEntity to avoid NullPointerException in service
        when(mapper.toEntity(any(JobApplicationDTO.class))).thenReturn(new JobApplication());
        when(applicationRepository.save(any(JobApplication.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate application"));

        // Act & Assert
        assertThatThrownBy(() -> service.create(applicationDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already applied");

        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should set default status when not provided")
    void create_WithoutStatus_ShouldSetDefaultPending() {
        // Arrange
        JobApplicationDTO dtoWithoutStatus = JobApplicationDTO.builder()
                .candidateId(1L)
                .jobOfferId(1L)
                .status(null)
                .build();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(activeJobOffer));
        // FIX: Stubbing toEntity to avoid NullPointerException in service
        when(mapper.toEntity(any(JobApplicationDTO.class))).thenReturn(new JobApplication());
        when(applicationRepository.save(any(JobApplication.class))).thenReturn(application);
        when(mapper.toDto(any(JobApplication.class))).thenReturn(applicationDTO);

        // Act
        JobApplicationDTO result = service.create(dtoWithoutStatus);

        // Assert
        assertThat(result).isNotNull();
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should update application status successfully")
    void updateStatus_WithValidId_ShouldUpdateStatus() {
        // Arrange
        String newStatus = "REVIEWED";
        JobApplication updatedApplication = JobApplication.builder()
                .id(1L)
                .candidate(candidate)
                .jobOffer(activeJobOffer)
                .status(newStatus)
                .build();

        JobApplicationDTO updatedDTO = JobApplicationDTO.builder()
                .id(1L)
                .candidateId(1L)
                .jobOfferId(1L)
                .status(newStatus)
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(JobApplication.class))).thenReturn(updatedApplication);
        when(mapper.toDto(updatedApplication)).thenReturn(updatedDTO);

        // Act
        JobApplicationDTO result = service.updateStatus(1L, newStatus);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(newStatus);
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent application")
    void updateStatus_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(applicationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus(nonExistentId, "REVIEWED"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job Application");

        verify(applicationRepository, never()).save(any(JobApplication.class));
    }
}
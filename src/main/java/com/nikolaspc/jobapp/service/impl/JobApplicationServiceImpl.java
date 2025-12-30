package com.nikolaspc.jobapp.service.impl;

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
import com.nikolaspc.jobapp.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of JobApplicationService with full CRUD operations.
 * Manages job applications with business rules:
 * - Prevents duplicate applications (same candidate + job offer)
 * - Validates job offer is active before allowing application
 * - Manages application status workflow
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final JobApplicationMapper mapper;

    private static final String DEFAULT_STATUS = "PENDING";

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationDTO> findAll() {
        log.info("Fetching all job applications");
        List<JobApplication> applications = applicationRepository.findAll();
        log.info("Found {} applications", applications.size());

        return applications.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationDTO findById(Long id) {
        log.info("Fetching job application with id: {}", id);

        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Job application not found with id: {}", id);
                    return new ResourceNotFoundException("Job Application", id);
                });

        log.info("Successfully retrieved application id: {}", application.getId());
        return mapper.toDto(application);
    }

    @Override
    @Transactional
    public JobApplicationDTO create(JobApplicationDTO dto) {
        log.info("Creating job application - Candidate: {}, Job Offer: {}",
                dto.getCandidateId(), dto.getJobOfferId());

        // Validate candidate exists
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> {
                    log.error("Candidate not found with id: {}", dto.getCandidateId());
                    return new ResourceNotFoundException("Candidate", dto.getCandidateId());
                });

        // Validate job offer exists
        JobOffer jobOffer = jobOfferRepository.findById(dto.getJobOfferId())
                .orElseThrow(() -> {
                    log.error("Job offer not found with id: {}", dto.getJobOfferId());
                    return new ResourceNotFoundException("Job Offer", dto.getJobOfferId());
                });

        // Business rule: Job offer must be active
        if (!jobOffer.isActive()) {
            log.warn("Attempted to apply to inactive job offer id: {}", jobOffer.getId());
            throw new BadRequestException(
                    "Cannot apply to inactive job offer: " + jobOffer.getTitle()
            );
        }

        // Build application entity
        JobApplication application = JobApplication.builder()
                .candidate(candidate)
                .jobOffer(jobOffer)
                .status(dto.getStatus() != null ? dto.getStatus() : DEFAULT_STATUS)
                .build();

        try {
            JobApplication savedApplication = applicationRepository.save(application);
            log.info("Successfully created application with id: {}", savedApplication.getId());
            return mapper.toDto(savedApplication);

        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate application attempt - Candidate: {}, Job Offer: {}",
                    dto.getCandidateId(), dto.getJobOfferId());
            throw new BadRequestException(
                    "Candidate has already applied to this job offer"
            );
        }
    }

    /**
     * Updates application status (e.g., PENDING -> REVIEWED -> ACCEPTED/REJECTED).
     * This method can be extended for complete update functionality.
     *
     * @param id application ID
     * @param newStatus the new status
     * @return updated application DTO
     */
    @Transactional
    public JobApplicationDTO updateStatus(Long id, String newStatus) {
        log.info("Updating application {} status to: {}", id, newStatus);

        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application", id));

        application.setStatus(newStatus);
        JobApplication updated = applicationRepository.save(application);

        log.info("Successfully updated application {} status", id);
        return mapper.toDto(updated);
    }
}
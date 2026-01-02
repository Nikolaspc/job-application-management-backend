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
        return applicationRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationDTO findById(Long id) {
        log.info("Fetching job application with id: {}", id);
        return applicationRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application", id));
    }

    @Override
    @Transactional
    public JobApplicationDTO create(JobApplicationDTO dto) {
        log.info("Creating application - Candidate ID: {}, Offer ID: {}", dto.getCandidateId(), dto.getJobOfferId());

        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", dto.getCandidateId()));

        JobOffer jobOffer = jobOfferRepository.findById(dto.getJobOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Job Offer", dto.getJobOfferId()));

        // Business Rule: Offer must be active
        // Logic updated to use getActive() because of Boolean type in Entity
        if (jobOffer.getActive() == null || !jobOffer.getActive()) {
            throw new BadRequestException("Cannot apply to inactive job offer: " + jobOffer.getTitle());
        }

        // Map basic fields and set relationships manually
        JobApplication application = mapper.toEntity(dto);
        application.setCandidate(candidate);
        application.setJobOffer(jobOffer);

        if (application.getStatus() == null) {
            application.setStatus(DEFAULT_STATUS);
        }

        try {
            return mapper.toDto(applicationRepository.save(application));
        } catch (DataIntegrityViolationException e) {
            log.error("Conflict: Candidate {} already applied to offer {}", dto.getCandidateId(), dto.getJobOfferId());
            throw new BadRequestException("Candidate has already applied to this job offer");
        }
    }

    @Override
    @Transactional
    public JobApplicationDTO updateStatus(Long id, String newStatus) {
        log.info("Updating application {} status to: {}", id, newStatus);
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application", id));

        application.setStatus(newStatus);
        return mapper.toDto(applicationRepository.save(application));
    }
}
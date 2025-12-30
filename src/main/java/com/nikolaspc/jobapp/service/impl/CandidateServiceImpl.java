package com.nikolaspc.jobapp.service.impl;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.exception.BadRequestException;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.CandidateMapper;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.service.CandidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CandidateService with full CRUD operations.
 * Provides business logic for candidate management including:
 * - Candidate registration and profile management
 * - Email uniqueness validation
 * - Age verification (must be 18+)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository repository;
    private final CandidateMapper mapper;

    private static final int MIN_AGE = 18;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDTO> findAll() {
        log.info("Fetching all candidates");
        List<Candidate> candidates = repository.findAll();
        log.info("Found {} candidates", candidates.size());

        return candidates.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateDTO findById(Long id) {
        log.info("Fetching candidate with id: {}", id);

        Candidate candidate = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Candidate not found with id: {}", id);
                    return new ResourceNotFoundException("Candidate", id);
                });

        log.info("Successfully retrieved candidate: {} {}",
                candidate.getFirstName(), candidate.getLastName());
        return mapper.toDto(candidate);
    }

    @Override
    @Transactional
    public CandidateDTO create(CandidateDTO dto) {
        log.info("Creating new candidate with email: {}", dto.getEmail());

        // Business validation: Check minimum age
        validateAge(dto.getDateOfBirth());

        // Convert DTO to entity
        Candidate candidate = mapper.toEntity(dto);

        try {
            Candidate savedCandidate = repository.save(candidate);
            log.info("Successfully created candidate with id: {}", savedCandidate.getId());
            return mapper.toDto(savedCandidate);

        } catch (DataIntegrityViolationException e) {
            log.error("Email already exists: {}", dto.getEmail());
            throw new BadRequestException(
                    "A candidate with email '" + dto.getEmail() + "' already exists"
            );
        }
    }

    /**
     * Validates that the candidate is at least 18 years old.
     *
     * @param dateOfBirth the candidate's birth date
     * @throws BadRequestException if candidate is under 18
     */
    private void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return; // Optional field
        }

        LocalDate today = LocalDate.now();
        int age = today.getYear() - dateOfBirth.getYear();

        // Adjust if birthday hasn't occurred this year
        if (today.getDayOfYear() < dateOfBirth.getDayOfYear()) {
            age--;
        }

        if (age < MIN_AGE) {
            log.warn("Candidate age validation failed. Age: {}, Required: {}", age, MIN_AGE);
            throw new BadRequestException(
                    "Candidate must be at least " + MIN_AGE + " years old"
            );
        }
    }
}
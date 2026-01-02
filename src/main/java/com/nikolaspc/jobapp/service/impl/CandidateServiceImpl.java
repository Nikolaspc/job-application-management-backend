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
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateDTO findById(Long id) {
        log.info("Fetching candidate with id: {}", id);
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
    }

    // Cambiado de 'create' a 'save' para cumplir con el contrato de la Interfaz
    @Override
    @Transactional
    public CandidateDTO save(CandidateDTO dto) {
        log.info("Saving candidate: {}", dto.getEmail());
        validateAge(dto.getDateOfBirth());
        Candidate candidate = mapper.toEntity(dto);
        try {
            Candidate savedCandidate = repository.save(candidate);
            return mapper.toDto(savedCandidate);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("A candidate with email '" + dto.getEmail() + "' already exists");
        }
    }

    @Override
    @Transactional
    public CandidateDTO update(Long id, CandidateDTO dto) {
        log.info("Updating candidate id: {}", id);
        Candidate candidate = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));

        validateAge(dto.getDateOfBirth());
        mapper.updateEntityFromDto(dto, candidate);

        try {
            Candidate updatedCandidate = repository.save(candidate);
            return mapper.toDto(updatedCandidate);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Email '" + dto.getEmail() + "' is already in use");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting candidate id: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate", id);
        }
        repository.deleteById(id);
    }

    private void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return;
        LocalDate today = LocalDate.now();
        int age = today.getYear() - dateOfBirth.getYear();
        if (today.getMonthValue() < dateOfBirth.getMonthValue() ||
                (today.getMonthValue() == dateOfBirth.getMonthValue() && today.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        if (age < MIN_AGE) {
            throw new BadRequestException("Candidate must be at least " + MIN_AGE + " years old");
        }
    }
}
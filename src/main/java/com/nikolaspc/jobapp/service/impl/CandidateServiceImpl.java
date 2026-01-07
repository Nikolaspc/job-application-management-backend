package com.nikolaspc.jobapp.service.impl;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.exception.BadRequestException;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.CandidateMapper;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.repository.UserRepository;
import com.nikolaspc.jobapp.service.CandidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;
    private final CandidateMapper mapper;

    private static final int MIN_AGE = 18;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDTO> findAll() {
        log.info("Fetching all candidates with their user data");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
    }

    @Override
    @Transactional
    public CandidateDTO save(CandidateDTO dto) {
        // English: In this normalized version, a Candidate is usually created via AuthService.
        // If created here, we must ensure the User master record exists first.
        throw new UnsupportedOperationException("Candidates must be registered via AuthService to ensure Identity integrity.");
    }

    @Override
    @Transactional
    public CandidateDTO update(Long id, CandidateDTO dto) {
        log.info("Updating candidate and user profile for ID: {}", id);

        Candidate candidate = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));

        validateAge(dto.getDateOfBirth());

        // 1. Update Candidate specific fields
        candidate.setDateOfBirth(dto.getDateOfBirth());

        // 2. Update User master fields (Normalizing the update)
        User user = candidate.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        // English: Logic check - If email changes, check for duplicates
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new BadRequestException("Email '" + dto.getEmail() + "' is already in use.");
            }
            user.setEmail(dto.getEmail());
        }

        Candidate updatedCandidate = repository.save(candidate);
        return mapper.toDto(updatedCandidate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting candidate and identity for ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        // English: Deleting the User will automatically delete the Candidate due to ON DELETE CASCADE
        userRepository.deleteById(id);
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
package com.nikolaspc.jobapp.service.impl;

import com.nikolaspc.jobapp.domain.JobOffer;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.mapper.JobOfferMapper;
import com.nikolaspc.jobapp.repository.JobOfferRepository;
import com.nikolaspc.jobapp.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Default: optimizado para lectura
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository repository;
    private final JobOfferMapper mapper;

    @Override
    public List<JobOfferResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList(); // .toList() es Java 16+, más limpio que Collectors.toList()
    }

    @Override
    public List<JobOfferResponseDTO> findActiveOffers() {
        return repository.findByActiveTrue().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public JobOfferResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Offer", id));
    }

    @Override
    @Transactional // Sobrescribe el readOnly=true de la clase
    public JobOfferResponseDTO create(JobOfferRequestDTO dto) {
        // El mapper ya setea active=true gracias a la config 'constant'
        JobOffer jobOffer = mapper.toEntity(dto);
        JobOffer savedOffer = repository.save(jobOffer);
        return mapper.toResponseDto(savedOffer);
    }

    @Override
    @Transactional
    public JobOfferResponseDTO update(Long id, JobOfferRequestDTO dto) {
        JobOffer jobOffer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Offer", id));

        mapper.updateEntityFromDto(dto, jobOffer);
        JobOffer updatedOffer = repository.save(jobOffer);
        return mapper.toResponseDto(updatedOffer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job Offer", id);
        }
        repository.deleteById(id);
    }
}
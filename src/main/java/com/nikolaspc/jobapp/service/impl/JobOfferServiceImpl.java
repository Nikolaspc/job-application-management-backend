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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository repository;
    private final JobOfferMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<JobOfferResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobOfferResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Offer not found with id: " + id));
    }

    @Override
    @Transactional
    public JobOfferResponseDTO create(JobOfferRequestDTO dto) {
        JobOffer entity = mapper.toEntity(dto);
        return mapper.toResponseDto(repository.save(entity));
    }

    @Override
    @Transactional
    public JobOfferResponseDTO update(Long id, JobOfferRequestDTO dto) {
        JobOffer existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Offer not found with id: " + id));

        mapper.updateEntityFromDto(dto, existingEntity);
        return mapper.toResponseDto(repository.save(existingEntity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job Offer not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
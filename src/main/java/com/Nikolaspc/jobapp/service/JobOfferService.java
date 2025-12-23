package com.Nikolaspc.jobapp.service;

import com.Nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.Nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import java.util.List;

public interface JobOfferService {
    List<JobOfferResponseDTO> findAll();
    JobOfferResponseDTO findById(Long id);
    JobOfferResponseDTO create(JobOfferRequestDTO dto);
    JobOfferResponseDTO update(Long id, JobOfferRequestDTO dto); // Nuevo
    void delete(Long id); // Nuevo
}
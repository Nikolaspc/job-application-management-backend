package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import java.util.List;

public interface JobOfferService {
    List<JobOfferResponseDTO> findAll();
    List<JobOfferResponseDTO> findActiveOffers(); // <--- Nuevo
    JobOfferResponseDTO findById(Long id);
    JobOfferResponseDTO create(JobOfferRequestDTO dto);
    JobOfferResponseDTO update(Long id, JobOfferRequestDTO dto);
    void delete(Long id);
}
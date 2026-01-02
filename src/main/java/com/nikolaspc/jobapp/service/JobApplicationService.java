package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import java.util.List;

public interface JobApplicationService {
    List<JobApplicationDTO> findAll();
    JobApplicationDTO findById(Long id);
    JobApplicationDTO create(JobApplicationDTO dto);
    // Añadimos esto para que coincida con tu implementación
    JobApplicationDTO updateStatus(Long id, String newStatus);
}
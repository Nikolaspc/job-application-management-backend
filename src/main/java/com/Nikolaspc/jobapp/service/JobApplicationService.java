package com.Nikolaspc.jobapp.service;

import com.Nikolaspc.jobapp.dto.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {

    List<JobApplicationDTO> findAll();

    JobApplicationDTO findById(Long id);

    JobApplicationDTO create(JobApplicationDTO dto);
}

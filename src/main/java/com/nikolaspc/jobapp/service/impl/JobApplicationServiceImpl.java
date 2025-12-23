package com.nikolaspc.jobapp.service.impl;

import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import com.nikolaspc.jobapp.service.JobApplicationService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
    @Override
    public List<JobApplicationDTO> findAll() { return new ArrayList<>(); }

    @Override
    public JobApplicationDTO findById(Long id) { return null; }

    @Override
    public JobApplicationDTO create(JobApplicationDTO dto) { return dto; }
}
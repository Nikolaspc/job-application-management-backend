package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.CandidateDTO;

import java.util.List;

public interface CandidateService {

    List<CandidateDTO> findAll();

    CandidateDTO findById(Long id);

    CandidateDTO create(CandidateDTO dto);
}

package com.Nikolaspc.jobapp.service;

import com.Nikolaspc.jobapp.dto.CandidateDTO;

import java.util.List;

public interface CandidateService {

    List<CandidateDTO> findAll();

    CandidateDTO findById(Long id);

    CandidateDTO create(CandidateDTO dto);
}

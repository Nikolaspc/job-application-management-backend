package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.CandidateDTO;
import java.util.List;

public interface CandidateService {
    List<CandidateDTO> findAll();
    CandidateDTO findById(Long id);
    CandidateDTO save(CandidateDTO dto); // Cambiamos 'create' por 'save' para que el Controller lo vea
    CandidateDTO update(Long id, CandidateDTO dto);
    void delete(Long id);
}
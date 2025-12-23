package com.Nikolaspc.jobapp.service.impl;

import com.Nikolaspc.jobapp.dto.CandidateDTO;
import com.Nikolaspc.jobapp.service.CandidateService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service // ESTO ES LO QUE LE FALTA A TU PROYECTO
public class CandidateServiceImpl implements CandidateService {

    @Override
    public List<CandidateDTO> findAll() {
        // Por ahora devolvemos una lista vacía para que arranque
        return new ArrayList<>();
    }

    @Override
    public CandidateDTO findById(Long id) {
        // Aquí irá la lógica de búsqueda en el futuro
        return null;
    }

    @Override
    public CandidateDTO create(CandidateDTO dto) {
        // Aquí irá la lógica de guardado en base de datos
        return dto;
    }
}
package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
        // Correctly calls .save() from CandidateService interface
        return new ResponseEntity<>(candidateService.save(candidateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id, @Valid @RequestBody CandidateDTO candidateDTO) {
        return ResponseEntity.ok(candidateService.update(id, candidateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
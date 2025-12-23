package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates", description = "Endpoints for candidate management")
public class CandidateController {

    private final CandidateService service;

    @GetMapping
    @Operation(summary = "Get all candidates", description = "Returns a list of all registered candidates")
    @ApiResponse(responseCode = "200", description = "Candidates list retrieved successfully")
    public ResponseEntity<List<CandidateDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get candidate by ID", description = "Returns a specific candidate by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    public ResponseEntity<CandidateDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create new candidate", description = "Creates a new candidate in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid candidate data")
    })
    public ResponseEntity<CandidateDTO> create(@RequestBody @Valid CandidateDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }
}
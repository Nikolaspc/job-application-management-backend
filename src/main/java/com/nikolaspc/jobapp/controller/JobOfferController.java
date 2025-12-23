package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.joboffer.JobOfferRequestDTO;
import com.nikolaspc.jobapp.dto.joboffer.JobOfferResponseDTO;
import com.nikolaspc.jobapp.service.JobOfferService;
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
@RequestMapping("/api/job-offers")
@RequiredArgsConstructor
@Tag(name = "Job Offers", description = "Endpoints for job offer management")
public class JobOfferController {

    private final JobOfferService service;

    @GetMapping
    @Operation(summary = "Get all job offers", description = "Returns a list of all job offers")
    @ApiResponse(responseCode = "200", description = "Job offers list retrieved successfully")
    public ResponseEntity<List<JobOfferResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job offer by ID", description = "Returns a specific job offer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job offer found"),
            @ApiResponse(responseCode = "404", description = "Job offer not found")
    })
    public ResponseEntity<JobOfferResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create new job offer", description = "Creates a new job offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job offer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid job offer data")
    })
    public ResponseEntity<JobOfferResponseDTO> create(@Valid @RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update job offer", description = "Updates an existing job offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job offer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Job offer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid job offer data")
    })
    public ResponseEntity<JobOfferResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job offer", description = "Deletes a job offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job offer not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Job Offers.
 * Provides endpoints for CRUD operations on job listings.
 */
@RestController
@RequestMapping("/api/jobs") // Updated to a cleaner, professional endpoint
@RequiredArgsConstructor
@Tag(name = "Job Offers", description = "Endpoints for job offer management")
public class JobOfferController {

    private final JobOfferService service;

    @GetMapping
    @Operation(summary = "Get all job offers", description = "Returns a list of all active job offers")
    @ApiResponse(responseCode = "200", description = "Job offers list retrieved successfully")
    public ResponseEntity<List<JobOfferResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job offer by ID", description = "Returns a specific job offer by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job offer found"),
            @ApiResponse(responseCode = "404", description = "Job offer not found")
    })
    public ResponseEntity<JobOfferResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create new job offer", description = "Creates and persists a new job offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job offer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid job offer data provided")
    })
    public ResponseEntity<JobOfferResponseDTO> create(@Valid @RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update job offer", description = "Updates an existing job offer record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job offer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Job offer not found"),
            @ApiResponse(responseCode = "400", description = "Validation failed for update data")
    })
    public ResponseEntity<JobOfferResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job offer", description = "Removes a job offer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job offer not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.nikolaspc.jobapp.controller;

import com.nikolaspc.jobapp.dto.JobApplicationDTO;
import com.nikolaspc.jobapp.service.JobApplicationService;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Endpoints for job application management")
public class JobApplicationController {

    private final JobApplicationService service;

    @GetMapping
    @Operation(summary = "Get all applications", description = "Returns a list of all job applications")
    @ApiResponse(responseCode = "200", description = "Applications list retrieved successfully")
    public ResponseEntity<List<JobApplicationDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID", description = "Returns a specific application by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application found"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<JobApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create new application", description = "Creates a new application to a job offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid application data")
    })
    public ResponseEntity<JobApplicationDTO> create(@RequestBody @Valid JobApplicationDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }
}
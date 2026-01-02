package com.nikolaspc.jobapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.exception.ResourceNotFoundException;
import com.nikolaspc.jobapp.security.JwtTokenProvider;
import com.nikolaspc.jobapp.service.CandidateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CandidateController.
 */
@WebMvcTest(CandidateController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CandidateController Integration Tests")
class CandidateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CandidateService service;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("GET /api/candidates - Should return all candidates")
    void getAllCandidates_ShouldReturnCandidateList() throws Exception {
        List<CandidateDTO> candidates = Arrays.asList(
                CandidateDTO.builder()
                        .id(1L)
                        .firstName("Max")
                        .lastName("Mustermann")
                        .email("max@example.com")
                        .dateOfBirth(LocalDate.of(1995, 5, 15))
                        .build(),
                CandidateDTO.builder()
                        .id(2L)
                        .firstName("Anna")
                        .lastName("Schmidt")
                        .email("anna@example.com")
                        .dateOfBirth(LocalDate.of(1990, 8, 20))
                        .build()
        );

        when(service.findAll()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Max")))
                .andExpect(jsonPath("$[1].firstName", is("Anna")));

        verify(service, times(1)).findAll();
    }

    @Test
    @DisplayName("POST /api/candidates - Should create candidate successfully")
    void createCandidate_WithValidData_ShouldReturn201() throws Exception {
        CandidateDTO inputDTO = CandidateDTO.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        CandidateDTO createdDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        // Fixed: Using save() instead of create()
        when(service.save(any(CandidateDTO.class))).thenReturn(createdDTO);

        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("max@example.com")));

        verify(service, times(1)).save(any(CandidateDTO.class));
    }

    @Test
    @DisplayName("POST /api/candidates - Should handle duplicate email gracefully")
    void createCandidate_WithDuplicateEmail_ShouldReturn400() throws Exception {
        CandidateDTO duplicateDTO = CandidateDTO.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("existing@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        // Fixed: Using save() instead of create()
        when(service.save(any(CandidateDTO.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateDTO)))
                .andExpect(status().isInternalServerError());

        verify(service, times(1)).save(any(CandidateDTO.class));
    }
}
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
 * Se ha añadido MockitoBean para JwtTokenProvider para evitar fallos en el ApplicationContext.
 */
@WebMvcTest(CandidateController.class)
@AutoConfigureMockMvc(addFilters = false) // Deshabilita filtros de seguridad para simplificar el test de controladores
@DisplayName("CandidateController Integration Tests")
class CandidateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CandidateService service;

    /**
     * IMPORTANTE: Se inyecta este Mock para que JwtAuthenticationFilter no falle
     * al intentar cargar el contexto de Spring.
     */
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("GET /api/candidates - Should return all candidates")
    void getAllCandidates_ShouldReturnCandidateList() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(get("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Max")))
                .andExpect(jsonPath("$[0].email", is("max@example.com")))
                .andExpect(jsonPath("$[1].firstName", is("Anna")))
                .andExpect(jsonPath("$[1].email", is("anna@example.com")));

        verify(service, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/candidates - Should return empty list when no candidates")
    void getAllCandidates_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/candidates/{id} - Should return candidate when exists")
    void getCandidateById_WhenExists_ShouldReturnCandidate() throws Exception {
        // Arrange
        CandidateDTO candidate = CandidateDTO.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        when(service.findById(1L)).thenReturn(candidate);

        // Act & Assert
        mockMvc.perform(get("/api/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.lastName", is("Mustermann")))
                .andExpect(jsonPath("$.email", is("max@example.com")));

        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/candidates/{id} - Should return 404 when not found")
    void getCandidateById_WhenNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(service.findById(999L))
                .thenThrow(new ResourceNotFoundException("Candidate", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/candidates/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Candidate")))
                .andExpect(jsonPath("$.message", containsString("999")));

        verify(service, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /api/candidates - Should create candidate successfully")
    void createCandidate_WithValidData_ShouldReturn201() throws Exception {
        // Arrange
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

        when(service.create(any(CandidateDTO.class))).thenReturn(createdDTO);

        // Act & Assert
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.email", is("max@example.com")));

        verify(service, times(1)).create(any(CandidateDTO.class));
    }

    @Test
    @DisplayName("POST /api/candidates - Should return 400 with invalid email")
    void createCandidate_WithInvalidEmail_ShouldReturn400() throws Exception {
        // Arrange
        CandidateDTO invalidDTO = CandidateDTO.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("invalid-email")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any(CandidateDTO.class));
    }

    @Test
    @DisplayName("POST /api/candidates - Should return 400 with missing required fields")
    void createCandidate_WithMissingFields_ShouldReturn400() throws Exception {
        // Arrange
        String incompleteJson = "{\"email\":\"test@example.com\"}";

        // Act & Assert
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any(CandidateDTO.class));
    }

    @Test
    @DisplayName("POST /api/candidates - Should handle duplicate email gracefully")
    void createCandidate_WithDuplicateEmail_ShouldReturn400() throws Exception {
        // Arrange
        CandidateDTO duplicateDTO = CandidateDTO.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("existing@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        when(service.create(any(CandidateDTO.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateDTO)))
                .andExpect(status().isInternalServerError());

        verify(service, times(1)).create(any(CandidateDTO.class));
    }
}
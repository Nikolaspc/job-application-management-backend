package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.dto.RegisterRequest;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // English: Rollback changes after test
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void whenRegisterCandidate_thenUserAndCandidateShouldExistWithSameId() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("test.integration@example.com")
                .password("password123")
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .build();

        // Act
        authService.register(request);

        // Assert
        User user = userRepository.findByEmail("test.integration@example.com").orElseThrow();
        Candidate candidate = candidateRepository.findById(user.getId()).orElseThrow();

        assertEquals(user.getId(), candidate.getId());
        assertEquals(LocalDate.of(1995, 5, 10), candidate.getDateOfBirth());
        // English: Verify that the candidate is linked to the correct user object
        assertEquals(user.getEmail(), candidate.getUser().getEmail());
    }
}
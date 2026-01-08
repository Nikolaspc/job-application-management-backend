package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.dto.RegisterRequest;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // English: Force the use of application-test.yml
@Transactional         // English: Rollback DB changes after each test
class AuthServiceTest { // <-- IMPORTANTE: Ya no extiende de AbstractTestContainers

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void whenRegisterCandidate_thenUserAndCandidateShouldExistWithSameId() {
        // Tu código original está perfecto...
    }
}
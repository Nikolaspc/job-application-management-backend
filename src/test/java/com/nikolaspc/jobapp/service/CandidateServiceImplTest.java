package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.dto.CandidateDTO;
import com.nikolaspc.jobapp.exception.BadRequestException;
import com.nikolaspc.jobapp.mapper.CandidateMapper;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.repository.UserRepository;
import com.nikolaspc.jobapp.service.impl.CandidateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidateService Unit Tests")
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository repository;

    @Mock
    private UserRepository userRepository; // Added to match service constructor

    @Mock
    private CandidateMapper mapper;

    @InjectMocks
    private CandidateServiceImpl service;

    private Candidate candidate;
    private CandidateDTO candidateDTO;

    @BeforeEach
    void setUp() {
        // English: Create the User master record first
        User user = User.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .build();

        // English: Link the User to the Candidate
        candidate = Candidate.builder()
                .id(1L)
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();

        candidateDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .build();
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException when saving directly")
    void save_ShouldThrowUnsupportedOperationException() {
        // English: Reflecting our architectural decision to use AuthService for creation
        assertThatThrownBy(() -> service.save(candidateDTO))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Candidates must be registered via AuthService");
    }

    // English: Note - If you have update tests, remember that candidate.getUser().getEmail()
    // is the way to access data now.
}
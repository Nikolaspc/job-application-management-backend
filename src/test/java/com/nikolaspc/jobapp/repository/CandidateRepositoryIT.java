package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CandidateRepositoryIT {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        candidateRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password")
                .role(UserRole.CANDIDATE)
                .active(true)
                .build();
        savedUser = userRepository.save(user);
    }

    @Test
    void shouldSaveAndFindCandidate() {
        // English: Updated .isNew(true) to .isNewCandidate(true) to match entity changes
        Candidate candidate = Candidate.builder()
                .user(savedUser)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .isNewCandidate(true)
                .build();

        Candidate savedCandidate = candidateRepository.save(candidate);

        Optional<Candidate> found = candidateRepository.findById(savedCandidate.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(found.get().getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldDeleteCandidateWhenUserIsDeleted() {
        Candidate candidate = Candidate.builder()
                .user(savedUser)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .isNewCandidate(true)
                .build();
        candidateRepository.save(candidate);

        userRepository.delete(savedUser);

        assertThat(candidateRepository.findById(savedUser.getId())).isEmpty();
    }
}
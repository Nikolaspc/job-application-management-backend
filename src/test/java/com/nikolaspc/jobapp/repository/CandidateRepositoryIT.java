package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.AbstractTestContainers;
import com.nikolaspc.jobapp.domain.Candidate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CandidateRepositoryIT extends AbstractTestContainers {

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void shouldSaveAndFindCandidateByEmail() {
        Candidate candidate = Candidate.builder()
                .firstName("Nikolas")
                .lastName("PC")
                .email("test@nikolaspc.com")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .build();

        candidateRepository.save(candidate);

        // This method must exist in CandidateRepository interface
        Optional<Candidate> found = candidateRepository.findByEmail("test@nikolaspc.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@nikolaspc.com");
    }
}
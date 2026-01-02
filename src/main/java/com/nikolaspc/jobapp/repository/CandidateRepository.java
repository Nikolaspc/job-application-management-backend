package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Candidate entity.
 * Provides standard CRUD and custom query methods.
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    /**
     * Finds a candidate by their unique email address.
     * Required by CandidateRepositoryIT and service logic.
     * * @param email The email to search for.
     * @return An Optional containing the candidate if found.
     */
    Optional<Candidate> findByEmail(String email);

    /**
     * Checks if a candidate exists with the given email.
     * * @param email The email to check.
     * @return true if it exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
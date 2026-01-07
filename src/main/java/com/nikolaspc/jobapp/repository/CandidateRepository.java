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
     * Finds a candidate by the email of their associated user.
     * English: We navigate from Candidate to User entity and then to the email field.
     * @param email The email to search for.
     * @return An Optional containing the candidate if found.
     */
    Optional<Candidate> findByUserEmail(String email);

    /**
     * Checks if a candidate exists with the given email through their user profile.
     * English: Required by service logic to prevent duplicate registrations.
     * @param email The email to check.
     * @return true if it exists, false otherwise.
     */
    boolean existsByUserEmail(String email);
}
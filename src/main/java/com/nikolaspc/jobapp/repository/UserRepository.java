package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 * Extends JpaRepository for basic CRUD operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with that email exists
     */
    boolean existsByEmail(String email);
}
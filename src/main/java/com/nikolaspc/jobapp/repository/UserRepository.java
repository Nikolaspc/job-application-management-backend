package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para User entity
 * Extiende JpaRepository para operaciones básicas CRUD
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Encuentra un usuario por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si un usuario con ese email existe
     */
    boolean existsByEmail(String email);
}
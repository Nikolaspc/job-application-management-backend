package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.AuthRequest;
import com.nikolaspc.jobapp.dto.AuthResponse;
import com.nikolaspc.jobapp.dto.RegisterRequest;
import com.nikolaspc.jobapp.dto.UserDto;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.domain.UserRole;
import com.nikolaspc.jobapp.domain.Candidate;
import com.nikolaspc.jobapp.exception.JwtException;
import com.nikolaspc.jobapp.exception.UserAlreadyExistsException;
import com.nikolaspc.jobapp.repository.UserRepository;
import com.nikolaspc.jobapp.repository.CandidateRepository;
import com.nikolaspc.jobapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // 1. Create and save User (Identity Master)
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.CANDIDATE)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // 2. Create Candidate Profile if role is CANDIDATE
        if (savedUser.getRole() == UserRole.CANDIDATE) {
            Candidate candidate = Candidate.builder()
                    .user(savedUser)
                    .dateOfBirth(request.getDateOfBirth() != null ?
                            request.getDateOfBirth() : LocalDate.of(1990, 1, 1))
                    .isNewCandidate(true) // English: Matches the renamed field in Candidate entity
                    .build();

            candidateRepository.save(candidate);
            log.info("Candidate profile created for User ID: {}", savedUser.getId());
        }

        return generateAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new JwtException("User not found"));

        if (!user.getActive() || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new JwtException("Invalid credentials");
        }
        return generateAuthResponse(user);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserFromToken(String token) {
        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            return userRepository.findByEmail(email).map(this::convertToDto);
        } catch (Exception e) {
            log.error("Error retrieving user from token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        UserDto userDto = convertToDto(user);
        String token = jwtTokenProvider.generateToken(user);
        return AuthResponse.of(userDto, token, jwtTokenProvider.getTokenExpirationSeconds());
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }
}
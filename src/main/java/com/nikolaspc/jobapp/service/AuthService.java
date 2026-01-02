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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // 1. Guardar Usuario
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.CANDIDATE)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // 2. Guardar Candidato si el rol es CANDIDATE
        if (savedUser.getRole() == UserRole.CANDIDATE) {
            Candidate candidate = Candidate.builder()
                    .id(savedUser.getId()) // Mismo ID que el User
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .dateOfBirth(request.getDateOfBirth() != null ?
                            request.getDateOfBirth() : LocalDate.of(1990, 1, 1))
                    .isNew(true) // Forzamos que Persistable entienda que es nuevo
                    .build();

            candidateRepository.save(candidate);
            log.info("Candidate profile created with ID: {}", savedUser.getId());
        }

        return generateAuthResponse(savedUser);
    }

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

    public Optional<UserDto> getUserFromToken(String token) {
        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            return userRepository.findByEmail(email).map(this::convertToDto);
        } catch (Exception e) {
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
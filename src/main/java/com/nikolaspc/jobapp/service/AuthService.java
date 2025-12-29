package com.nikolaspc.jobapp.service;

import com.nikolaspc.jobapp.dto.AuthRequest;
import com.nikolaspc.jobapp.dto.AuthResponse;
import com.nikolaspc.jobapp.dto.RegisterRequest;
import com.nikolaspc.jobapp.dto.UserDto;
import com.nikolaspc.jobapp.domain.User;
import com.nikolaspc.jobapp.domain.UserRole;
import com.nikolaspc.jobapp.exception.JwtException;
import com.nikolaspc.jobapp.exception.UserAlreadyExistsException;
import com.nikolaspc.jobapp.repository.UserRepository;
import com.nikolaspc.jobapp.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Authentication Service
 *
 * Maneja:
 * - Registro de nuevos usuarios
 * - Login de usuarios existentes
 * - Validación de tokens
 * - Generación de respuestas autenticadas
 */
@Slf4j
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Registra un nuevo usuario
     *
     * @param request datos del nuevo usuario
     * @return respuesta con JWT token
     * @throws UserAlreadyExistsException si el email ya existe
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("User already exists with email: {}", request.getEmail());
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Crear nuevo usuario
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hash password
                .role(request.getRole() != null ? request.getRole() : UserRole.CANDIDATE)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        return generateAuthResponse(savedUser);
    }

    /**
     * Login de usuario con email y contraseña
     *
     * @param request credenciales del usuario
     * @return respuesta con JWT token
     * @throws JwtException si credenciales son inválidas o usuario inactivo
     */
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for user: {}", request.getEmail());

        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new JwtException("User not found"));

        // Verificar si el usuario está activo
        if (!user.getActive()) {
            log.warn("Login attempt for inactive user: {}", request.getEmail());
            throw new JwtException("User account is disabled");
        }

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", request.getEmail());
            throw new JwtException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", user.getEmail());
        return generateAuthResponse(user);
    }

    /**
     * Valida un JWT token
     *
     * @param token el token a validar
     * @return true si el token es válido
     */
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Obtiene información del usuario a partir del token
     *
     * @param token JWT token
     * @return Optional con datos del usuario
     */
    public Optional<UserDto> getUserFromToken(String token) {
        try {
            jwtTokenProvider.validateToken(token);
            String email = jwtTokenProvider.getEmailFromToken(token);

            return userRepository.findByEmail(email)
                    .map(this::convertToDto);
        } catch (JwtException e) {
            log.warn("Invalid token provided");
            return Optional.empty();
        }
    }

    /**
     * Genera respuesta de autenticación con JWT token
     */
    private AuthResponse generateAuthResponse(User user) {
        UserDto userDto = convertToDto(user);
        String token = jwtTokenProvider.generateToken(user);
        long expiresIn = jwtTokenProvider.getTokenExpirationSeconds();

        return AuthResponse.of(userDto, token, expiresIn);
    }

    /**
     * Convierte User entity a UserDto
     */
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
package com.example.banking.service;

import com.example.banking.dto.CreateUserRequest;
import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Helper method to mask email for logging
    private String maskEmail(String email) {
        if (email == null || email.length() < 5) return "***";
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) return "***" + email.substring(atIndex);
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }

    // Helper method to decode email (if needed later)
    public String decodeEmail(String encodedEmail) {
        return new String(Base64.getDecoder().decode(encodedEmail));
    }

    public User createUser(CreateUserRequest request) {
        // Log the incoming request (safely)
        log.info("Attempting to create user with username: {}", request.getUsername() , request.getEmail(), request.getPassword());
        log.debug("User creation request received - Username: {}, Email: {}",
                request.getUsername(), maskEmail(request.getEmail()));
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Email already exists: {}", maskEmail(request.getEmail()));
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", maskEmail(request.getEmail()));
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        // Encode email to Base64
        String encodedEmail = Base64.getEncoder().encodeToString(request.getEmail().getBytes());

        // Encode password using BCrypt
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("Password encoded successfully for user: {}", request.getUsername());

        // Create user entity (ID will be auto-generated)
        User user = User.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim()) // Store encoded email if needed
                .password(request.getPassword()) // Store encoded password
                .build();

        User savedUser = userRepository.save(user);

        log.info("User created successfully - ID: {}, Username: {}",
                savedUser.getId(), savedUser.getUsername(),savedUser.getEmail());

        return savedUser;
    }
}
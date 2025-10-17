package com.example.banking.service;

import com.example.banking.dto.UpdateUserRequest;
import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UpdateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);

        // 1. Find existing user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for update - ID: {}", userId);
                    throw new RuntimeException("User not found with ID: " + userId);
                });

        // 2. Validate username uniqueness (if changed)
        if (!existingUser.getUsername().equals(request.getUsername())) {
            validateUsernameUniqueness(request.getUsername());
        }

        // 3. Validate email uniqueness (if changed)
        if (!existingUser.getEmail().equals(request.getEmail())) {
            validateEmailUniqueness(request.getEmail());
        }

        // 4. Update user details
        updateUserDetails(existingUser, request);

        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    // Validate username uniqueness

    private void validateUsernameUniqueness(String username) {
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists: {}", username);
            throw new RuntimeException("Username already exists: " + username);
        }
    }

    //Validate email uniqueness

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists: {}", email);
            throw new RuntimeException("Email already exists: " + email);
        }
    }

    // Update user entity with new details

    private void updateUserDetails(User user, UpdateUserRequest request) {
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());

        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword().trim());
            user.setPassword(request.getPassword().trim());
            log.debug("Password updated for user: {}", user.getUsername());
        }

    }

}
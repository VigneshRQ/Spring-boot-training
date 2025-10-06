package com.example.banking.service;

import com.example.banking.dto.AuthenticationRequest;
import com.example.banking.dto.AuthenticationResponse;
import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final OtpService otpService;
    private final UserRepository userRepository;
    // Authenticate user and send OTP
    public AuthenticationResponse loginUser(AuthenticationRequest request) {
        try {
            log.info("Login attempt for username: {}", request.getUsername());

            // Authenticate user credentials
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            log.debug("Authentication successful for user: {}", request.getUsername());

            // Get user email and send OTP also no need to care on the username returning multiple records since they ate unique in creation
            String userEmail = userRepository.findByUsername(request.getUsername())
                    .map(User::getEmail)
                    .orElseThrow(() -> {
                        log.error("User not found after successful authentication: {}", request.getUsername());
                        return new RuntimeException("User email not found");
                    });

//            return AuthenticationResponse.success("User logged in successfully!");
            // Generate and send OTP
//            if(!userEmail.isEmpty()){
//                return AuthenticationResponse.success("Logged in successfully!");
//            }
                boolean otpSent = otpService.generateAndSendOtp(userEmail, request.getUsername());
                if (otpSent) {
                    log.info("OTP sent successfully to: {}", maskEmail(userEmail));
                    return AuthenticationResponse.success("OTP sent to your email. Please verify.");
                } else {
                    log.error("Failed to send OTP to: {}", maskEmail(userEmail));
                    return AuthenticationResponse.error("Failed to send OTP. Please try again later.");
                }
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", request.getUsername(), e);
            return AuthenticationResponse.error("Invalid username or password");
        } catch (RuntimeException e) {
            log.error("Business logic error during login: {}", e.getMessage());
            return AuthenticationResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", request.getUsername(), e);
            return AuthenticationResponse.error("Login failed due to technical error");
        }
    }

    //Mask email for logging security
    private String maskEmail(String email) {
        if (email == null || email.length() < 5) return "***";
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) return "***" + email.substring(atIndex);
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}

package com.example.banking.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class AuthenticationResponse {
    private boolean success;
    private String message;
    private String sessionToken; // For future use if needed

    public AuthenticationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthenticationResponse(boolean success, String message, String sessionToken) {
        this.success = success;
        this.message = message;
        this.sessionToken = sessionToken;
    }

    // Success responses
    public static AuthenticationResponse success(String message) {
        return new AuthenticationResponse(true, message);
    }

    public static AuthenticationResponse success(String message, String sessionToken) {
        return new AuthenticationResponse(true, message, sessionToken);
    }

    // Error responses
    public static AuthenticationResponse error(String message) {
        return new AuthenticationResponse(false, message);
    }

    // Convert to ResponseEntity for controller
    public ResponseEntity<String> toResponseEntity() {
        HttpStatus status = success ? HttpStatus.OK :
                message.contains("Invalid credentials") ? HttpStatus.UNAUTHORIZED :
                        HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(message);
    }

    public ResponseEntity<ApiResponse<?>> toApiResponse() {
        HttpStatus status = success ? HttpStatus.OK :
                message.contains("Invalid credentials") ? HttpStatus.UNAUTHORIZED :
                        HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(success ?
                ApiResponse.success(message) : ApiResponse.error(message));
    }
}

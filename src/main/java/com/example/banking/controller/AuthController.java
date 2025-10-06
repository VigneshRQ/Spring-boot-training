package com.example.banking.controller;

import com.example.banking.dto.AuthenticationRequest;
import com.example.banking.dto.AuthenticationResponse;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {
    private final UserRepository repo;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody AuthenticationRequest req){
        log.info("Login endpoint called for user: {}", req.getUsername());

        AuthenticationResponse authResponse = authService.loginUser(req);

        return authResponse.toApiResponse();
    }
}

package com.example.banking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "User Name is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;
}

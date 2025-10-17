package com.example.banking.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteUserRequest {

    @NotNull(message = "User ID is required")
    private Long id;

}

package com.example.banking.controller;

import com.example.banking.dto.*;
import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.CreateUserService;
import com.example.banking.service.UpdateUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserRepository repo;
    private final CreateUserService userService;
    private final UpdateUserService userUpdateService;

    @GetMapping("/user-list")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<User> users = repo.findAll();
            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully!", userResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unable to retrieve users!"));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<?>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        try {
            User createdUser = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully! ", createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unable to create user!"));
        }
    }

    @GetMapping("/user-detail")
    public ResponseEntity<ApiResponse<?>> findByUsername(
            @NotBlank @RequestParam String username
    ) {
        try {
            List<User> users = repo.findByUsernameContaining(username.trim());

            if (users.isEmpty()) {
                throw new RuntimeException("No users found with username: " + username);
            }

            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Users found successfully", userResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user-info/{id}")
    public ResponseEntity<ApiResponse<?>> findByUserId(
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id
    ) {
        try {
            User user = repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found!"));
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/edit-user/{id}")
    public ResponseEntity<ApiResponse<?>> updateUserDetails(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest userRequest
    ) {
        try {
            User updatedUser = userUpdateService.updateUser(id, userRequest);
            UserResponse response = UserResponse.fromUser(updatedUser);

            return ResponseEntity.ok(ApiResponse.success("User updated successfully!", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unable to update user!"));
        }
    }


    @DeleteMapping("/delete-user")
    public ResponseEntity<ApiResponse<?>> deleteByUserName(
            @Valid @RequestBody DeleteUserRequest request
    ) {
        try {
            User user = repo.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("User not found!"));
            repo.delete(user);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Unable to delete user!"));
        }
    }
}

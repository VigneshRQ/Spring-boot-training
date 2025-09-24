package com.example.banking.controller;

import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repo;

    @GetMapping("/user-list")
    @ResponseBody
    public List<User> all() {
        return  repo.findAll();
    }

    @PostMapping("/create-user")
    public User create(@RequestBody User user) {
        return repo.save(user);
    }

    @GetMapping("/user-detail")
    public ResponseEntity<?> findByUsername(@RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username parameter is required");
        }

        return repo.findByUsername(username.trim())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with username: " + username));
    }

}

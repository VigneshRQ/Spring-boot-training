package com.example.banking.controller;
import com.example.banking.model.User;
import com.example.banking.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;

//    @PostMapping
//    public User create(@RequestBody User user) {
//        User u  = User.builder().id(1L).username("Lucifer").email("check+123@gmail.com").password("Password@123").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
//        return userRepo.save(u);
//    }

//    @GetMapping("/health")
//    public String health() {
//        return "Application is running!";
//    }

    @GetMapping("/{id}")
    public User findByUserId(@PathVariable Long id) {
        User u = new User();
        return userRepo.findById(id).orElse(u);
    }
}

package com.example.blood_backend.controller;

import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.repository.UserSignupRepository;
import com.example.blood_backend.service.UserSignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signup")
@CrossOrigin(origins = "http://localhost:5173") // React frontend
public class UserSignupController {

    private final UserSignupService service;

    public UserSignupController(UserSignupService service) {
        this.service = service;
    }

    // Register user
    @PostMapping
    public UserSignup registerUser(@RequestBody UserSignup user) {
        return service.registerUser(user);
    }

    // Get all registered users
    @GetMapping
    public List<UserSignup> getAllUsers() {
        return service.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public UserSignup getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }


}

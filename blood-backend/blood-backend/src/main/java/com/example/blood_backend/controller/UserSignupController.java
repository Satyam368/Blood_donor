package com.example.blood_backend.controller;

import com.example.blood_backend.entity.UserSignup;
import jakarta.validation.Valid;
import com.example.blood_backend.service.UserSignupService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signup")
@CrossOrigin(origins = {"http://localhost:8083", "http://localhost:8080", "http://localhost:3001", "http://localhost:5173"}) // React frontend on multiple ports
public class UserSignupController {

    private final UserSignupService service;

    public UserSignupController(UserSignupService service) {
        this.service = service;
    }

    // Register user
    @PostMapping
    public UserSignup registerUser(@Valid @RequestBody UserSignup user) {
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

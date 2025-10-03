package com.example.blood_backend.controller;

import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.dto.LoginRequest;
import jakarta.validation.Valid;
import com.example.blood_backend.service.UserSignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // React frontend
public class UserController {

    private final UserSignupService service;

    public UserController(UserSignupService service) {
        this.service = service;
    }

    // ✅ Register user (Signup)
    @PostMapping("/signup")
    public UserSignup registerUser(@Valid @RequestBody UserSignup user) {
        return service.registerUser(user);
    }

    // ✅ Login user
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserSignup user = service.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("message", "Login Successful ✅");
            // Optionally add a token or user info here
            // response.put("token", "your-jwt-token");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid Email or Password ❌");
            return ResponseEntity.status(401).body(response);
        }
    }

    // ✅ Get all registered users
    @GetMapping
    public List<UserSignup> getAllUsers() {
        return service.getAllUsers();
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public UserSignup getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }
}

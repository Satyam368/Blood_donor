package com.example.blood_backend.service;

import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.entity.User;
import com.example.blood_backend.repository.UserSignupRepository;
import com.example.blood_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserSignupService {

    private final UserSignupRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignupService(UserSignupRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSignup registerUser(UserSignup user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save the detailed user signup information
        UserSignup savedUser = repository.save(user);
        
        // Also create a simple User record for login authentication
        User loginUser = new User();
        loginUser.setEmail(user.getEmail());
        loginUser.setPassword(user.getPassword()); // Already hashed
        loginUser.setRememberMe(false);
        userRepository.save(loginUser);
        
        return savedUser;
    }

    public List<UserSignup> getAllUsers() {
        return repository.findAll();
    }

    public UserSignup getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }
    // ✅ Login service
    public UserSignup loginUser(String email, String password) {
        UserSignup user = repository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}

package com.example.blood_backend.service;

import com.example.blood_backend.entity.User;
import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.repository.UserRepository;
import com.example.blood_backend.repository.UserSignupRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSignupRepository userSignupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserSignupRepository userSignupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userSignupRepository = userSignupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean loginUser(String email, String password) {
        // First check in the User table (for direct registrations)
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null && passwordEncoder.matches(password, existingUser.getPassword())) {
            return true; // valid credentials
        }
        
        // Also check in the UserSignup table (for detailed signups)
        UserSignup existingSignupUser = userSignupRepository.findByEmail(email);
        if (existingSignupUser != null && passwordEncoder.matches(password, existingSignupUser.getPassword())) {
            return true; // valid credentials
        }
        
        return false;
    }
}

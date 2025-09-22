package com.example.blood_backend.service;

import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.entity.User;
import com.example.blood_backend.repository.UserSignupRepository;
import com.example.blood_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        // Hash the password before persisting
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        UserSignup savedUser = repository.save(user);

        User loginUser = new User();
        loginUser.setEmail(user.getEmail());
        loginUser.setPassword(savedUser.getPassword());
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
    // ✅ Login service - verify using PasswordEncoder
    public UserSignup loginUser(String email, String password) {
        UserSignup stored = repository.findByEmail(email);
        if (stored != null && passwordEncoder.matches(password, stored.getPassword())) {
            return stored;
        }
        return null;
    }
}

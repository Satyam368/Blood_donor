package com.example.blood_backend.service;

import com.example.blood_backend.entity.UserSignup;
import com.example.blood_backend.repository.UserSignupRepository;
import com.example.blood_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSignupServiceBCryptTest {

    @Mock
    private UserSignupRepository userSignupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserSignupService userSignupService;

    @Test
    void registerUser_ShouldHashPassword() {
        // Given
        UserSignup user = new UserSignup();
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");

        String hashedPassword = "$2a$10$hashedPasswordExample";
        
        when(userSignupRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn(hashedPassword);
        when(userSignupRepository.save(any(UserSignup.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(null);

        // When
        userSignupService.registerUser(user);

        // Then
        verify(passwordEncoder).encode("plainPassword");
        assertEquals(hashedPassword, user.getPassword());
    }

    @Test
    void loginUser_ShouldVerifyHashedPassword() {
        // Given
        String email = "test@example.com";
        String plainPassword = "plainPassword";
        String hashedPassword = "$2a$10$hashedPasswordExample";

        UserSignup user = new UserSignup();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userSignupRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(true);

        // When
        UserSignup result = userSignupService.loginUser(email, plainPassword);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(passwordEncoder).matches(plainPassword, hashedPassword);
    }

    @Test
    void loginUser_ShouldReturnNull_WhenPasswordIncorrect() {
        // Given
        String email = "test@example.com";
        String plainPassword = "wrongPassword";
        String hashedPassword = "$2a$10$hashedPasswordExample";

        UserSignup user = new UserSignup();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userSignupRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(false);

        // When
        UserSignup result = userSignupService.loginUser(email, plainPassword);

        // Then
        assertNull(result);
        verify(passwordEncoder).matches(plainPassword, hashedPassword);
    }
}
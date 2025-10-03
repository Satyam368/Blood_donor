package com.example.blood_backend.repository;

import com.example.blood_backend.entity.UserSignup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSignupRepository extends JpaRepository<UserSignup, Long> {
    boolean existsByEmail(String email);
    UserSignup findByEmail(String email);
}

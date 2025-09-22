package com.example.blood_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_signup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bloodType;
    private String dateOfBirth;

    // Location
    private String address;
    private String city;
    private String zipCode;

    // Account Security
    private String password;

    // Medical Information
    @Column(length = 1000)
    private String medicalConditions;

    private boolean isDonor;

    // Terms
    private boolean acceptTerms;
    private boolean acceptPrivacy;

    // Manual getters and setters to ensure they're available
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

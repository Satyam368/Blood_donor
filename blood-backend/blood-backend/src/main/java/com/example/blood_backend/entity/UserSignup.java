package com.example.blood_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(
        regexp = "^[+]?([0-9][ -]?){7,15}[0-9]$",
        message = "Phone number must be 8-16 digits and may include spaces or dashes"
    )
    @Column(nullable = false)
    private String phone;
    private String bloodType;
    private String dateOfBirth;

    // Location
    private String address;
    private String city;
    @Pattern(
        regexp = "^[0-9A-Za-z -]{3,10}$",
        message = "ZIP/Postal code must be 3-10 characters (letters, numbers, space or -)"
    )
    private String zipCode;

    // Account Security
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be 8-64 characters long")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}:;',\"|<.>/?`~]).{8,64}$",
        message = "Password must include upper, lower, number and special character"
    )
    @Column(nullable = false)
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

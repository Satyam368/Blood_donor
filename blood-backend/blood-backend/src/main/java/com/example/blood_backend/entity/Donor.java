package com.example.blood_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[+]?(?:\\d[ -]?){7,15}\\d$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Blood type is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood type must be one of A+, A-, B+, B-, AB+, AB-, O+, O-")
    private String bloodType;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    // Medical Information
    @NotNull(message = "Weight is required")
    @DecimalMin(value = "45.0", message = "Minimum eligible weight is 45 kg")
    @DecimalMax(value = "200.0", message = "Weight must be realistic (<= 200 kg)")
    private Double weight;
    private String medicalConditions;
    private LocalDate lastDonation;

    // Address
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^[0-9A-Za-z -]{3,10}$", message = "Invalid ZIP/Postal code")
    private String zipCode;

    // Emergency
    @NotBlank(message = "Emergency contact is required")
    private String emergencyContact;

    @NotBlank(message = "Emergency phone is required")
    @Pattern(regexp = "^[+]?(?:\\d[ -]?){7,15}\\d$", message = "Invalid emergency phone number")
    private String emergencyPhone;

    // Preferences
    @AssertTrue(message = "You must agree to the terms")
    private boolean agreeToTerms;
    private boolean availableForEmergency;
}

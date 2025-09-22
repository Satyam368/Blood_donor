package com.example.blood_backend.entity;

import jakarta.persistence.*;
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
    private String fullName;
    private String email;
    private String phone;
    private String bloodType;
    private LocalDate dateOfBirth;

    // Medical Information
    private Double weight;
    private String medicalConditions;
    private LocalDate lastDonation;

    // Address
    private String address;
    private String city;
    private String state;
    private String zipCode;

    // Emergency
    private String emergencyContact;
    private String emergencyPhone;

    // Preferences
    private boolean agreeToTerms;
    private boolean availableForEmergency;
}

package com.example.blood_backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Donor Entity Tests")
class DonorTest {

    private Donor donor;

    @BeforeEach
    void setUp() {
        donor = Donor.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john.doe@email.com")
                .phone("+1234567890")
                .bloodType("O+")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .weight(70.0)
                .medicalConditions("None")
                .lastDonation(LocalDate.of(2024, 6, 1))
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .emergencyContact("Jane Doe")
                .emergencyPhone("+1234567891")
                .agreeToTerms(true)
                .availableForEmergency(true)
                .build();
    }

    @Test
    @DisplayName("Should create donor with all fields")
    void createDonor_WithAllFields_ShouldWork() {
        // Then
        assertThat(donor.getId()).isEqualTo(1L);
        assertThat(donor.getFullName()).isEqualTo("John Doe");
        assertThat(donor.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(donor.getPhone()).isEqualTo("+1234567890");
        assertThat(donor.getBloodType()).isEqualTo("O+");
        assertThat(donor.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(donor.getWeight()).isEqualTo(70.0);
        assertThat(donor.getMedicalConditions()).isEqualTo("None");
        assertThat(donor.getLastDonation()).isEqualTo(LocalDate.of(2024, 6, 1));
        assertThat(donor.getAddress()).isEqualTo("123 Main St");
        assertThat(donor.getCity()).isEqualTo("New York");
        assertThat(donor.getState()).isEqualTo("NY");
        assertThat(donor.getZipCode()).isEqualTo("10001");
        assertThat(donor.getEmergencyContact()).isEqualTo("Jane Doe");
        assertThat(donor.getEmergencyPhone()).isEqualTo("+1234567891");
        assertThat(donor.isAgreeToTerms()).isTrue();
        assertThat(donor.isAvailableForEmergency()).isTrue();
    }

    @Test
    @DisplayName("Should create donor with no-args constructor")
    void createDonor_WithNoArgsConstructor_ShouldWork() {
        // When
        Donor emptyDonor = new Donor();

        // Then
        assertThat(emptyDonor).isNotNull();
        assertThat(emptyDonor.getId()).isNull();
        assertThat(emptyDonor.getFullName()).isNull();
        assertThat(emptyDonor.getEmail()).isNull();
        assertThat(emptyDonor.getPhone()).isNull();
        assertThat(emptyDonor.getBloodType()).isNull();
        assertThat(emptyDonor.getDateOfBirth()).isNull();
        assertThat(emptyDonor.getWeight()).isNull();
        assertThat(emptyDonor.getMedicalConditions()).isNull();
        assertThat(emptyDonor.getLastDonation()).isNull();
        assertThat(emptyDonor.getAddress()).isNull();
        assertThat(emptyDonor.getCity()).isNull();
        assertThat(emptyDonor.getState()).isNull();
        assertThat(emptyDonor.getZipCode()).isNull();
        assertThat(emptyDonor.getEmergencyContact()).isNull();
        assertThat(emptyDonor.getEmergencyPhone()).isNull();
        assertThat(emptyDonor.isAgreeToTerms()).isFalse();
        assertThat(emptyDonor.isAvailableForEmergency()).isFalse();
    }

    @Test
    @DisplayName("Should create donor with all-args constructor")
    void createDonor_WithAllArgsConstructor_ShouldWork() {
        // When
        Donor newDonor = new Donor(
                2L,
                "Jane Smith",
                "jane.smith@email.com",
                "+1987654321",
                "A+",
                LocalDate.of(1985, 5, 15),
                65.0,
                "Allergic to aspirin",
                LocalDate.of(2024, 5, 1),
                "456 Oak Ave",
                "Los Angeles",
                "CA",
                "90001",
                "John Smith",
                "+1987654322",
                true,
                false
        );

        // Then
        assertThat(newDonor.getId()).isEqualTo(2L);
        assertThat(newDonor.getFullName()).isEqualTo("Jane Smith");
        assertThat(newDonor.getEmail()).isEqualTo("jane.smith@email.com");
        assertThat(newDonor.getPhone()).isEqualTo("+1987654321");
        assertThat(newDonor.getBloodType()).isEqualTo("A+");
        assertThat(newDonor.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 5, 15));
        assertThat(newDonor.getWeight()).isEqualTo(65.0);
        assertThat(newDonor.getMedicalConditions()).isEqualTo("Allergic to aspirin");
        assertThat(newDonor.getLastDonation()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(newDonor.getAddress()).isEqualTo("456 Oak Ave");
        assertThat(newDonor.getCity()).isEqualTo("Los Angeles");
        assertThat(newDonor.getState()).isEqualTo("CA");
        assertThat(newDonor.getZipCode()).isEqualTo("90001");
        assertThat(newDonor.getEmergencyContact()).isEqualTo("John Smith");
        assertThat(newDonor.getEmergencyPhone()).isEqualTo("+1987654322");
        assertThat(newDonor.isAgreeToTerms()).isTrue();
        assertThat(newDonor.isAvailableForEmergency()).isFalse();
    }

    @Test
    @DisplayName("Should support setter methods")
    void setterMethods_ShouldWork() {
        // Given
        Donor newDonor = new Donor();

        // When
        newDonor.setId(3L);
        newDonor.setFullName("Bob Johnson");
        newDonor.setEmail("bob@email.com");
        newDonor.setPhone("+1555555555");
        newDonor.setBloodType("B+");
        newDonor.setDateOfBirth(LocalDate.of(1992, 8, 20));
        newDonor.setWeight(80.0);
        newDonor.setMedicalConditions("Diabetes");
        newDonor.setLastDonation(LocalDate.of(2024, 4, 1));
        newDonor.setAddress("789 Pine St");
        newDonor.setCity("Chicago");
        newDonor.setState("IL");
        newDonor.setZipCode("60001");
        newDonor.setEmergencyContact("Alice Johnson");
        newDonor.setEmergencyPhone("+1555555556");
        newDonor.setAgreeToTerms(true);
        newDonor.setAvailableForEmergency(true);

        // Then
        assertThat(newDonor.getId()).isEqualTo(3L);
        assertThat(newDonor.getFullName()).isEqualTo("Bob Johnson");
        assertThat(newDonor.getEmail()).isEqualTo("bob@email.com");
        assertThat(newDonor.getPhone()).isEqualTo("+1555555555");
        assertThat(newDonor.getBloodType()).isEqualTo("B+");
        assertThat(newDonor.getDateOfBirth()).isEqualTo(LocalDate.of(1992, 8, 20));
        assertThat(newDonor.getWeight()).isEqualTo(80.0);
        assertThat(newDonor.getMedicalConditions()).isEqualTo("Diabetes");
        assertThat(newDonor.getLastDonation()).isEqualTo(LocalDate.of(2024, 4, 1));
        assertThat(newDonor.getAddress()).isEqualTo("789 Pine St");
        assertThat(newDonor.getCity()).isEqualTo("Chicago");
        assertThat(newDonor.getState()).isEqualTo("IL");
        assertThat(newDonor.getZipCode()).isEqualTo("60001");
        assertThat(newDonor.getEmergencyContact()).isEqualTo("Alice Johnson");
        assertThat(newDonor.getEmergencyPhone()).isEqualTo("+1555555556");
        assertThat(newDonor.isAgreeToTerms()).isTrue();
        assertThat(newDonor.isAvailableForEmergency()).isTrue();
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCode_ShouldWork() {
        // Given
        Donor donor1 = Donor.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@email.com")
                .bloodType("O+")
                .build();

        Donor donor2 = Donor.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@email.com")
                .bloodType("O+")
                .build();

        Donor donor3 = Donor.builder()
                .id(2L)
                .fullName("Jane Smith")
                .email("jane@email.com")
                .bloodType("A+")
                .build();

        // Then
        assertThat(donor1).isEqualTo(donor2);
        assertThat(donor1).isNotEqualTo(donor3);
        assertThat(donor1.hashCode()).isEqualTo(donor2.hashCode());
        assertThat(donor1.hashCode()).isNotEqualTo(donor3.hashCode());
    }

    @Test
    @DisplayName("Should support toString method")
    void toString_ShouldWork() {
        // When
        String donorString = donor.toString();

        // Then
        assertThat(donorString).isNotNull();
        assertThat(donorString).contains("John Doe");
        assertThat(donorString).contains("john.doe@email.com");
        assertThat(donorString).contains("O+");
        assertThat(donorString).contains("New York");
    }

    @Test
    @DisplayName("Should handle null values gracefully")
    void nullValues_ShouldBeHandledGracefully() {
        // Given
        Donor donorWithNulls = Donor.builder()
                .fullName("Test Donor")
                .email("test@email.com")
                .bloodType("AB+")
                .phone(null)
                .dateOfBirth(null)
                .weight(null)
                .medicalConditions(null)
                .lastDonation(null)
                .address(null)
                .city(null)
                .state(null)
                .zipCode(null)
                .emergencyContact(null)
                .emergencyPhone(null)
                .agreeToTerms(true)
                .availableForEmergency(false)
                .build();

        // Then
        assertThat(donorWithNulls.getFullName()).isEqualTo("Test Donor");
        assertThat(donorWithNulls.getEmail()).isEqualTo("test@email.com");
        assertThat(donorWithNulls.getBloodType()).isEqualTo("AB+");
        assertThat(donorWithNulls.getPhone()).isNull();
        assertThat(donorWithNulls.getDateOfBirth()).isNull();
        assertThat(donorWithNulls.getWeight()).isNull();
        assertThat(donorWithNulls.getMedicalConditions()).isNull();
        assertThat(donorWithNulls.getLastDonation()).isNull();
        assertThat(donorWithNulls.getAddress()).isNull();
        assertThat(donorWithNulls.getCity()).isNull();
        assertThat(donorWithNulls.getState()).isNull();
        assertThat(donorWithNulls.getZipCode()).isNull();
        assertThat(donorWithNulls.getEmergencyContact()).isNull();
        assertThat(donorWithNulls.getEmergencyPhone()).isNull();
        assertThat(donorWithNulls.isAgreeToTerms()).isTrue();
        assertThat(donorWithNulls.isAvailableForEmergency()).isFalse();
    }

    @Test
    @DisplayName("Should create donor with minimal required fields")
    void createDonor_WithMinimalFields_ShouldWork() {
        // When
        Donor minimalDonor = Donor.builder()
                .fullName("Minimal Donor")
                .email("minimal@email.com")
                .bloodType("O-")
                .agreeToTerms(true)
                .build();

        // Then
        assertThat(minimalDonor.getFullName()).isEqualTo("Minimal Donor");
        assertThat(minimalDonor.getEmail()).isEqualTo("minimal@email.com");
        assertThat(minimalDonor.getBloodType()).isEqualTo("O-");
        assertThat(minimalDonor.isAgreeToTerms()).isTrue();
        assertThat(minimalDonor.isAvailableForEmergency()).isFalse(); // default value
    }
}
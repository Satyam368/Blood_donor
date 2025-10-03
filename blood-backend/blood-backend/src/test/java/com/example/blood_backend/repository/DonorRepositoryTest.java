package com.example.blood_backend.repository;

import com.example.blood_backend.entity.Donor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("DonorRepository Integration Tests")
class DonorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DonorRepository donorRepository;

    private Donor donor1;
    private Donor donor2;
    private Donor donor3;

    @BeforeEach
    void setUp() {
        // Create test donors
        donor1 = Donor.builder()
                .fullName("John Doe")
                .email("john@email.com")
                .phone("+1234567890")
                .bloodType("O+")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .weight(70.0)
                .medicalConditions("None")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .emergencyContact("Jane Doe")
                .emergencyPhone("+1234567891")
                .agreeToTerms(true)
                .availableForEmergency(true)
                .build();

        donor2 = Donor.builder()
                .fullName("Jane Smith")
                .email("jane@email.com")
                .phone("+1234567892")
                .bloodType("A+")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .weight(65.0)
                .medicalConditions("None")
                .address("456 Oak Ave")
                .city("New York")
                .state("NY")
                .zipCode("10002")
                .emergencyContact("John Smith")
                .emergencyPhone("+1234567893")
                .agreeToTerms(true)
                .availableForEmergency(false)
                .build();

        donor3 = Donor.builder()
                .fullName("Bob Johnson")
                .email("bob@email.com")
                .phone("+1234567894")
                .bloodType("O+")
                .dateOfBirth(LocalDate.of(1992, 8, 20))
                .weight(80.0)
                .medicalConditions("None")
                .address("789 Pine St")
                .city("Los Angeles")
                .state("CA")
                .zipCode("90001")
                .emergencyContact("Alice Johnson")
                .emergencyPhone("+1234567895")
                .agreeToTerms(true)
                .availableForEmergency(true)
                .build();

        // Persist test data
        entityManager.persistAndFlush(donor1);
        entityManager.persistAndFlush(donor2);
        entityManager.persistAndFlush(donor3);
    }

    @Test
    @DisplayName("Should save and retrieve donor successfully")
    void saveAndRetrieveDonor_ShouldWork() {
        // Given
        Donor newDonor = Donor.builder()
                .fullName("Test Donor")
                .email("test@email.com")
                .bloodType("B+")
                .city("Chicago")
                .agreeToTerms(true)
                .build();

        // When
        Donor savedDonor = donorRepository.save(newDonor);

        // Then
        assertThat(savedDonor.getId()).isNotNull();
        assertThat(savedDonor.getFullName()).isEqualTo("Test Donor");
        assertThat(savedDonor.getEmail()).isEqualTo("test@email.com");
        assertThat(savedDonor.getBloodType()).isEqualTo("B+");
        assertThat(savedDonor.getCity()).isEqualTo("Chicago");
    }

    @Test
    @DisplayName("Should find all donors")
    void findAll_ShouldReturnAllDonors() {
        // When
        List<Donor> donors = donorRepository.findAll();

        // Then
        assertThat(donors).hasSize(3);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith", "Bob Johnson");
    }

    @Test
    @DisplayName("Should search by city only")
    void searchByCityAndBloodType_WithCityOnly_ShouldReturnMatchingDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType("New York", null);

        // Then
        assertThat(donors).hasSize(2);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
        assertThat(donors).allMatch(donor -> donor.getCity().equals("New York"));
    }

    @Test
    @DisplayName("Should search by blood type only")
    void searchByCityAndBloodType_WithBloodTypeOnly_ShouldReturnMatchingDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType(null, "O+");

        // Then
        assertThat(donors).hasSize(2);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Bob Johnson");
        assertThat(donors).allMatch(donor -> donor.getBloodType().equals("O+"));
    }

    @Test
    @DisplayName("Should search by both city and blood type")
    void searchByCityAndBloodType_WithBothParameters_ShouldReturnMatchingDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType("New York", "O+");

        // Then
        assertThat(donors).hasSize(1);
        assertThat(donors.get(0).getFullName()).isEqualTo("John Doe");
        assertThat(donors.get(0).getCity()).isEqualTo("New York");
        assertThat(donors.get(0).getBloodType()).isEqualTo("O+");
    }

    @Test
    @DisplayName("Should handle case insensitive city search")
    void searchByCityAndBloodType_WithDifferentCase_ShouldReturnMatchingDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType("new york", null);

        // Then
        assertThat(donors).hasSize(2);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("Should handle partial city match")
    void searchByCityAndBloodType_WithPartialCity_ShouldReturnMatchingDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType("York", null);

        // Then
        assertThat(donors).hasSize(2);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("Should handle blood type with spaces")
    void searchByCityAndBloodType_WithBloodTypeSpaces_ShouldReturnMatchingDonors() {
        // Given - create donor with spaced blood type
        Donor spacedBloodTypeDonor = Donor.builder()
                .fullName("Test Donor")
                .email("test@email.com")
                .bloodType("O +")  // Note the space
                .city("Test City")
                .agreeToTerms(true)
                .build();
        entityManager.persistAndFlush(spacedBloodTypeDonor);

        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType(null, "O+");

        // Then
        assertThat(donors).hasSize(3); // Original 2 O+ donors + 1 "O +" donor
        assertThat(donors).extracting(Donor::getFullName)
                .contains("Test Donor");
    }

    @Test
    @DisplayName("Should return empty list when no matches found")
    void searchByCityAndBloodType_WithNoMatches_ShouldReturnEmptyList() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType("Non-existent City", "AB-");

        // Then
        assertThat(donors).isEmpty();
    }

    @Test
    @DisplayName("Should return all donors when both parameters are null")
    void searchByCityAndBloodType_WithBothNull_ShouldReturnAllDonors() {
        // When
        List<Donor> donors = donorRepository.searchByCityAndBloodType(null, null);

        // Then
        assertThat(donors).hasSize(3);
        assertThat(donors).extracting(Donor::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith", "Bob Johnson");
    }
}
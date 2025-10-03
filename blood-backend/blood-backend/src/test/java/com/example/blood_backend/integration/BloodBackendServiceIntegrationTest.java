package com.example.blood_backend.integration;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.repository.DonorRepository;
import com.example.blood_backend.service.DonorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Blood Backend Service Integration Tests")
class BloodBackendServiceIntegrationTest {

    @Autowired
    private DonorService donorService;

    @Autowired
    private DonorRepository donorRepository;

    @BeforeEach
    void setUp() {
        donorRepository.deleteAll();
    }

    @Test
    @DisplayName("Should complete full donor registration and search flow")
    void completeBloodDonationFlow_ShouldWork() {
        // Step 1: Register a new donor
        Donor newDonor = createTestDonor("Integration Test Donor", "integration@test.com", "O+", "Test City");
        
        Donor savedDonor = donorService.saveDonor(newDonor);
        
        // Verify donor was saved
        assertThat(savedDonor).isNotNull();
        assertThat(savedDonor.getId()).isNotNull();
        assertThat(savedDonor.getFullName()).isEqualTo("Integration Test Donor");
        assertThat(savedDonor.getEmail()).isEqualTo("integration@test.com");
        assertThat(savedDonor.getBloodType()).isEqualTo("O+");
        assertThat(savedDonor.getCity()).isEqualTo("Test City");

        // Step 2: Verify donor exists in database
        assertThat(donorRepository.count()).isEqualTo(1);

        // Step 3: Get all donors
        List<Donor> allDonors = donorService.getAllDonors();
        assertThat(allDonors).hasSize(1);
        assertThat(allDonors.get(0).getFullName()).isEqualTo("Integration Test Donor");

        // Step 4: Search for donors by city
        List<Donor> donorsByCity = donorService.findDonors("Test City", null);
        assertThat(donorsByCity).hasSize(1);
        assertThat(donorsByCity.get(0).getFullName()).isEqualTo("Integration Test Donor");
        assertThat(donorsByCity.get(0).getCity()).isEqualTo("Test City");

        // Step 5: Search for donors by blood type
        List<Donor> donorsByBloodType = donorService.findDonors(null, "O+");
        assertThat(donorsByBloodType).hasSize(1);
        assertThat(donorsByBloodType.get(0).getBloodType()).isEqualTo("O+");

        // Step 6: Search for donors by both city and blood type
        List<Donor> donorsByCityAndBloodType = donorService.findDonors("Test City", "O+");
        assertThat(donorsByCityAndBloodType).hasSize(1);
        assertThat(donorsByCityAndBloodType.get(0).getFullName()).isEqualTo("Integration Test Donor");

        // Step 7: Search with no matches
        List<Donor> noMatches = donorService.findDonors("Non-existent City", "AB-");
        assertThat(noMatches).isEmpty();
    }

    @Test
    @DisplayName("Should handle multiple donors registration and search")
    void multipleDonorsFlow_ShouldWork() {
        // Register multiple donors with different blood types and cities
        Donor donor1 = createTestDonor("John Doe", "john@email.com", "O+", "New York");
        Donor donor2 = createTestDonor("Jane Smith", "jane@email.com", "A+", "New York");
        Donor donor3 = createTestDonor("Bob Johnson", "bob@email.com", "O+", "Los Angeles");

        // Register all donors
        donorService.saveDonor(donor1);
        donorService.saveDonor(donor2);
        donorService.saveDonor(donor3);

        // Verify all donors are saved
        assertThat(donorRepository.count()).isEqualTo(3);

        // Test various search scenarios
        
        // Search by city "New York" - should return 2 donors
        List<Donor> newYorkDonors = donorService.findDonors("New York", null);
        assertThat(newYorkDonors).hasSize(2);
        assertThat(newYorkDonors).extracting(Donor::getCity)
                .allMatch(city -> city.equals("New York"));

        // Search by blood type "O+" - should return 2 donors
        List<Donor> oPlusDonors = donorService.findDonors(null, "O+");
        assertThat(oPlusDonors).hasSize(2);
        assertThat(oPlusDonors).extracting(Donor::getBloodType)
                .allMatch(bloodType -> bloodType.equals("O+"));

        // Search by specific city and blood type - should return 1 donor
        List<Donor> specificDonors = donorService.findDonors("New York", "O+");
        assertThat(specificDonors).hasSize(1);
        assertThat(specificDonors.get(0).getFullName()).isEqualTo("John Doe");

        // Search with no parameters - should return all 3 donors
        List<Donor> allDonors = donorService.findDonors(null, null);
        assertThat(allDonors).hasSize(3);
    }

    @Test
    @DisplayName("Should handle case insensitive and partial city search")
    void caseInsensitiveSearch_ShouldWork() {
        // Register a donor
        Donor donor = createTestDonor("Test Donor", "test@email.com", "B+", "Chicago");
        donorService.saveDonor(donor);

        // Search with different cases - these should all work due to the repository's case-insensitive search
        List<Donor> results1 = donorService.findDonors("chicago", null);  // lowercase
        List<Donor> results2 = donorService.findDonors("CHICAGO", null);  // uppercase
        List<Donor> results3 = donorService.findDonors("Chi", null);      // partial match

        assertThat(results1).hasSize(1);
        assertThat(results2).hasSize(1);
        assertThat(results3).hasSize(1);
        
        assertThat(results1.get(0).getFullName()).isEqualTo("Test Donor");
        assertThat(results2.get(0).getFullName()).isEqualTo("Test Donor");
        assertThat(results3.get(0).getFullName()).isEqualTo("Test Donor");
    }

    @Test
    @DisplayName("Should handle blood type with spaces")
    void bloodTypeWithSpaces_ShouldWork() {
        // Register donor with blood type having spaces
        Donor donor = createTestDonor("Space Test", "space@email.com", "A +", "Boston");
        donorService.saveDonor(donor);

        // Search for "A+" (without space) should find the donor with "A +" (with space)
        List<Donor> results = donorService.findDonors(null, "A+");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFullName()).isEqualTo("Space Test");
    }

    @Test
    @DisplayName("Should validate data persistence across operations")
    void dataPersistence_ShouldWork() {
        // Register a donor
        Donor originalDonor = createTestDonor("Persistence Test", "persist@email.com", "AB+", "Seattle");
        Donor savedDonor = donorService.saveDonor(originalDonor);
        
        assertThat(savedDonor.getId()).isNotNull();

        // Verify the donor persists across multiple operations
        for (int i = 0; i < 3; i++) {
            List<Donor> donors = donorService.getAllDonors();
            assertThat(donors).hasSize(1);
            assertThat(donors.get(0).getId()).isEqualTo(savedDonor.getId());
            assertThat(donors.get(0).getFullName()).isEqualTo("Persistence Test");
        }

        // Verify database state
        assertThat(donorRepository.count()).isEqualTo(1);
        Donor dbDonor = donorRepository.findById(savedDonor.getId()).orElse(null);
        assertThat(dbDonor).isNotNull();
        assertThat(dbDonor.getFullName()).isEqualTo("Persistence Test");
        assertThat(dbDonor.getEmail()).isEqualTo("persist@email.com");
        assertThat(dbDonor.getBloodType()).isEqualTo("AB+");
        assertThat(dbDonor.getCity()).isEqualTo("Seattle");
    }

    @Test
    @DisplayName("Should handle edge cases and empty searches")
    void edgeCases_ShouldWork() {
        // Test with empty database
        List<Donor> emptyResults = donorService.getAllDonors();
        assertThat(emptyResults).isEmpty();

        List<Donor> emptySearch = donorService.findDonors("Any City", "Any Type");
        assertThat(emptySearch).isEmpty();

        // Add some donors
        donorService.saveDonor(createTestDonor("Test 1", "test1@email.com", "O+", "City A"));
        donorService.saveDonor(createTestDonor("Test 2", "test2@email.com", "A-", "City B"));

        // Test null and empty searches
        List<Donor> nullCitySearch = donorService.findDonors(null, "O+");
        assertThat(nullCitySearch).hasSize(1);

        List<Donor> emptyCitySearch = donorService.findDonors("", "O+");
        assertThat(emptyCitySearch).hasSize(1); // Empty string treated as null

        List<Donor> blankCitySearch = donorService.findDonors("   ", "O+");
        assertThat(blankCitySearch).hasSize(1); // Blank string treated as null
    }

    private Donor createTestDonor(String name, String email, String bloodType, String city) {
        return Donor.builder()
                .fullName(name)
                .email(email)
                .phone("+1234567890")
                .bloodType(bloodType)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .weight(70.0)
                .medicalConditions("None")
                .address("123 Test St")
                .city(city)
                .state("TS")
                .zipCode("12345")
                .emergencyContact("Emergency Contact")
                .emergencyPhone("+1234567891")
                .agreeToTerms(true)
                .availableForEmergency(true)
                .build();
    }
}
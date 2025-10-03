package com.example.blood_backend.integration;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.repository.DonorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Blood Backend Integration Tests")
class BloodBackendIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        donorRepository.deleteAll();
    }

    @Test
    @DisplayName("Should complete basic donor registration and retrieval")
    void basicDonorFlow_ShouldWork() throws Exception {
        // Step 1: Register a new donor
        Donor newDonor = createTestDonor("Integration Test Donor", "integration@test.com", "O+", "Test City");

        // Register the donor
        mockMvc.perform(post("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDonor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName", is("Integration Test Donor")))
                .andExpect(jsonPath("$.email", is("integration@test.com")))
                .andExpect(jsonPath("$.bloodType", is("O+")))
                .andExpect(jsonPath("$.city", is("Test City")));

        // Step 2: Verify donor was saved in database
        assertThat(donorRepository.count()).isEqualTo(1);

        // Step 3: Get all donors
        mockMvc.perform(get("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName", is("Integration Test Donor")));
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
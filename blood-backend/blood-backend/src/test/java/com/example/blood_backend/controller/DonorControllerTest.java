package com.example.blood_backend.controller;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.service.DonorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DonorController.class)
@ActiveProfiles("test")
@DisplayName("DonorController Web Layer Tests")
class DonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonorService donorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Donor testDonor;

    @BeforeEach
    void setUp() {
        testDonor = Donor.builder()
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
    @DisplayName("Should register donor successfully")
    void registerDonor_WithValidDonor_ShouldReturnCreatedDonor() throws Exception {
        // Given
        Donor donorToRegister = Donor.builder()
                .fullName("John Doe")
                .email("john.doe@email.com")
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

        when(donorService.saveDonor(any(Donor.class))).thenReturn(testDonor);

        // When & Then
        mockMvc.perform(post("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(donorToRegister)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@email.com")))
                .andExpect(jsonPath("$.phone", is("+1234567890")))
                .andExpect(jsonPath("$.bloodType", is("O+")))
                .andExpect(jsonPath("$.dateOfBirth", is("1990-01-01")))
                .andExpect(jsonPath("$.weight", is(70.0)))
                .andExpect(jsonPath("$.medicalConditions", is("None")))
                .andExpect(jsonPath("$.lastDonation", is("2024-06-01")))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.city", is("New York")))
                .andExpect(jsonPath("$.state", is("NY")))
                .andExpect(jsonPath("$.zipCode", is("10001")))
                .andExpect(jsonPath("$.emergencyContact", is("Jane Doe")))
                .andExpect(jsonPath("$.emergencyPhone", is("+1234567891")))
                .andExpect(jsonPath("$.agreeToTerms", is(true)))
                .andExpect(jsonPath("$.availableForEmergency", is(true)));
    }

    @Test
    @DisplayName("Should handle invalid JSON in donor registration")
    void registerDonor_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get all donors successfully")
    void getDonors_ShouldReturnAllDonors() throws Exception {
        // Given
        Donor secondDonor = Donor.builder()
                .id(2L)
                .fullName("Jane Smith")
                .email("jane.smith@email.com")
                .bloodType("A+")
                .city("Los Angeles")
                .agreeToTerms(true)
                .build();

        List<Donor> donors = Arrays.asList(testDonor, secondDonor);
        when(donorService.getAllDonors()).thenReturn(donors);

        // When & Then
        mockMvc.perform(get("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@email.com")))
                .andExpect(jsonPath("$[0].bloodType", is("O+")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].email", is("jane.smith@email.com")))
                .andExpect(jsonPath("$[1].bloodType", is("A+")));
    }

    @Test
    @DisplayName("Should return empty list when no donors exist")
    void getDonors_WhenNoDonorsExist_ShouldReturnEmptyList() throws Exception {
        // Given
        when(donorService.getAllDonors()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle CORS preflight request")
    void handleCorsPreflight_ShouldAllowCrossOrigin() throws Exception {
        mockMvc.perform(options("/api/donors")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("Should register donor with minimal required fields")
    void registerDonor_WithMinimalFields_ShouldSucceed() throws Exception {
        // Given
        Donor minimalDonor = Donor.builder()
                .fullName("Minimal Donor")
                .email("minimal@email.com")
                .bloodType("B+")
                .agreeToTerms(true)
                .build();

        Donor savedMinimalDonor = Donor.builder()
                .id(3L)
                .fullName("Minimal Donor")
                .email("minimal@email.com")
                .bloodType("B+")
                .agreeToTerms(true)
                .build();

        when(donorService.saveDonor(any(Donor.class))).thenReturn(savedMinimalDonor);

        // When & Then
        mockMvc.perform(post("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalDonor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.fullName", is("Minimal Donor")))
                .andExpect(jsonPath("$.email", is("minimal@email.com")))
                .andExpect(jsonPath("$.bloodType", is("B+")))
                .andExpect(jsonPath("$.agreeToTerms", is(true)));
    }

    @Test
    @DisplayName("Should register donor with null optional fields")
    void registerDonor_WithNullOptionalFields_ShouldSucceed() throws Exception {
        // Given
        Donor donorWithNulls = Donor.builder()
                .fullName("Test Donor")
                .email("test@email.com")
                .bloodType("AB+")
                .phone(null)
                .medicalConditions(null)
                .lastDonation(null)
                .agreeToTerms(true)
                .availableForEmergency(false)
                .build();

        Donor savedDonor = Donor.builder()
                .id(4L)
                .fullName("Test Donor")
                .email("test@email.com")
                .bloodType("AB+")
                .agreeToTerms(true)
                .availableForEmergency(false)
                .build();

        when(donorService.saveDonor(any(Donor.class))).thenReturn(savedDonor);

        // When & Then
        mockMvc.perform(post("/api/donors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(donorWithNulls)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.fullName", is("Test Donor")))
                .andExpect(jsonPath("$.email", is("test@email.com")))
                .andExpect(jsonPath("$.bloodType", is("AB+")))
                .andExpect(jsonPath("$.agreeToTerms", is(true)))
                .andExpect(jsonPath("$.availableForEmergency", is(false)));
    }
}
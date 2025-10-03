package com.example.blood_backend.controller;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.service.DonorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FindDonorController.class)
@ActiveProfiles("test")
@DisplayName("FindDonorController Web Layer Tests")
class FindDonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonorService donorService;

    private List<Donor> testDonors;

    @BeforeEach
    void setUp() {
        Donor donor1 = Donor.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@email.com")
                .bloodType("O+")
                .city("New York")
                .state("NY")
                .agreeToTerms(true)
                .build();

        Donor donor2 = Donor.builder()
                .id(2L)
                .fullName("Jane Smith")
                .email("jane@email.com")
                .bloodType("A+")
                .city("New York")
                .state("NY")
                .agreeToTerms(true)
                .build();

        Donor donor3 = Donor.builder()
                .id(3L)
                .fullName("Bob Johnson")
                .email("bob@email.com")
                .bloodType("O+")
                .city("Los Angeles")
                .state("CA")
                .agreeToTerms(true)
                .build();

        testDonors = Arrays.asList(donor1, donor2, donor3);
    }

    @Test
    @DisplayName("Should search donors by city only")
    void searchDonors_WithCityOnly_ShouldReturnMatchingDonors() throws Exception {
        // Given
        List<Donor> newYorkDonors = testDonors.stream()
                .filter(donor -> "New York".equals(donor.getCity()))
                .toList();
        
        when(donorService.findDonors("New York", null))
                .thenReturn(newYorkDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "New York")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[0].city", is("New York")))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].city", is("New York")));
    }

    @Test
    @DisplayName("Should search donors by blood type only")
    void searchDonors_WithBloodTypeOnly_ShouldReturnMatchingDonors() throws Exception {
        // Given
        List<Donor> oPlusDonors = testDonors.stream()
                .filter(donor -> "O+".equals(donor.getBloodType()))
                .toList();
        
        when(donorService.findDonors(null, "O+"))
                .thenReturn(oPlusDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("bloodType", "O+")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[0].bloodType", is("O+")))
                .andExpect(jsonPath("$[1].fullName", is("Bob Johnson")))
                .andExpect(jsonPath("$[1].bloodType", is("O+")));
    }

    @Test
    @DisplayName("Should search donors by both city and blood type")
    void searchDonors_WithCityAndBloodType_ShouldReturnMatchingDonors() throws Exception {
        // Given
        List<Donor> specificDonors = testDonors.stream()
                .filter(donor -> "New York".equals(donor.getCity()) && "O+".equals(donor.getBloodType()))
                .toList();
        
        when(donorService.findDonors("New York", "O+"))
                .thenReturn(specificDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "New York")
                        .param("bloodType", "O+")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[0].city", is("New York")))
                .andExpect(jsonPath("$[0].bloodType", is("O+")));
    }

    @Test
    @DisplayName("Should search donors without parameters")
    void searchDonors_WithoutParameters_ShouldReturnAllDonors() throws Exception {
        // Given
        when(donorService.findDonors(null, null))
                .thenReturn(testDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")))
                .andExpect(jsonPath("$[2].fullName", is("Bob Johnson")));
    }

    @Test
    @DisplayName("Should return empty list when no donors match criteria")
    void searchDonors_WithNoMatches_ShouldReturnEmptyList() throws Exception {
        // Given
        when(donorService.findDonors("Non-existent City", "AB-"))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "Non-existent City")
                        .param("bloodType", "AB-")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle empty string parameters")
    void searchDonors_WithEmptyStringParameters_ShouldTreatAsNull() throws Exception {
        // Given - The controller receives empty strings, not null
        when(donorService.findDonors("", ""))
                .thenReturn(testDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "")
                        .param("bloodType", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("Should handle whitespace-only parameters")
    void searchDonors_WithWhitespaceParameters_ShouldTreatAsNull() throws Exception {
        // Given - The controller receives whitespace strings, not null
        when(donorService.findDonors("   ", "  "))
                .thenReturn(testDonors);

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "   ")
                        .param("bloodType", "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("Should handle special characters in search parameters")
    void searchDonors_WithSpecialCharacters_ShouldWork() throws Exception {
        // Given
        when(donorService.findDonors("St. John's", "O+"))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/find-donors/search")
                        .param("city", "St. John's")
                        .param("bloodType", "O+")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle CORS preflight request")
    void handleCorsPreflight_ShouldAllowCrossOrigin() throws Exception {
        mockMvc.perform(options("/api/find-donors/search")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
package com.example.blood_backend.service;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.repository.DonorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DonorService Unit Tests")
class DonorServiceTest {

    @Mock
    private DonorRepository donorRepository;

    @InjectMocks
    private DonorService donorService;

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
    @DisplayName("Should save donor successfully")
    void saveDonor_ShouldReturnSavedDonor() {
        // Given
        when(donorRepository.save(any(Donor.class))).thenReturn(testDonor);

        // When
        Donor savedDonor = donorService.saveDonor(testDonor);

        // Then
        assertThat(savedDonor).isNotNull();
        assertThat(savedDonor.getId()).isEqualTo(1L);
        assertThat(savedDonor.getFullName()).isEqualTo("John Doe");
        assertThat(savedDonor.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(savedDonor.getBloodType()).isEqualTo("O+");
        
        verify(donorRepository, times(1)).save(testDonor);
    }

    @Test
    @DisplayName("Should retrieve all donors successfully")
    void getAllDonors_ShouldReturnAllDonors() {
        // Given
        Donor secondDonor = Donor.builder()
                .id(2L)
                .fullName("Jane Smith")
                .email("jane.smith@email.com")
                .bloodType("A+")
                .city("Los Angeles")
                .build();

        List<Donor> expectedDonors = Arrays.asList(testDonor, secondDonor);
        when(donorRepository.findAll()).thenReturn(expectedDonors);

        // When
        List<Donor> actualDonors = donorService.getAllDonors();

        // Then
        assertThat(actualDonors).isNotNull();
        assertThat(actualDonors).hasSize(2);
        assertThat(actualDonors).containsExactlyInAnyOrder(testDonor, secondDonor);
        
        verify(donorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find donors by city and blood type")
    void findDonors_WithCityAndBloodType_ShouldReturnMatchingDonors() {
        // Given
        String city = "New York";
        String bloodType = "O+";
        List<Donor> expectedDonors = Arrays.asList(testDonor);
        
        when(donorRepository.searchByCityAndBloodType(city, bloodType))
                .thenReturn(expectedDonors);

        // When
        List<Donor> actualDonors = donorService.findDonors(city, bloodType);

        // Then
        assertThat(actualDonors).isNotNull();
        assertThat(actualDonors).hasSize(1);
        assertThat(actualDonors.get(0)).isEqualTo(testDonor);
        
        verify(donorRepository, times(1))
                .searchByCityAndBloodType(city, bloodType);
    }

    @Test
    @DisplayName("Should handle null city parameter")
    void findDonors_WithNullCity_ShouldPassNullToRepository() {
        // Given
        String bloodType = "O+";
        when(donorRepository.searchByCityAndBloodType(null, bloodType))
                .thenReturn(Arrays.asList(testDonor));

        // When
        List<Donor> actualDonors = donorService.findDonors(null, bloodType);

        // Then
        assertThat(actualDonors).isNotNull();
        verify(donorRepository, times(1))
                .searchByCityAndBloodType(null, bloodType);
    }

    @Test
    @DisplayName("Should handle blank city parameter")
    void findDonors_WithBlankCity_ShouldPassNullToRepository() {
        // Given
        String blankCity = "   ";
        String bloodType = "O+";
        when(donorRepository.searchByCityAndBloodType(null, bloodType))
                .thenReturn(Arrays.asList(testDonor));

        // When
        List<Donor> actualDonors = donorService.findDonors(blankCity, bloodType);

        // Then
        assertThat(actualDonors).isNotNull();
        verify(donorRepository, times(1))
                .searchByCityAndBloodType(null, bloodType);
    }

    @Test
    @DisplayName("Should handle null blood type parameter")
    void findDonors_WithNullBloodType_ShouldPassNullToRepository() {
        // Given
        String city = "New York";
        when(donorRepository.searchByCityAndBloodType(city, null))
                .thenReturn(Arrays.asList(testDonor));

        // When
        List<Donor> actualDonors = donorService.findDonors(city, null);

        // Then
        assertThat(actualDonors).isNotNull();
        verify(donorRepository, times(1))
                .searchByCityAndBloodType(city, null);
    }

    @Test
    @DisplayName("Should handle blank blood type parameter")
    void findDonors_WithBlankBloodType_ShouldPassNullToRepository() {
        // Given
        String city = "New York";
        String blankBloodType = "";
        when(donorRepository.searchByCityAndBloodType(city, null))
                .thenReturn(Arrays.asList(testDonor));

        // When
        List<Donor> actualDonors = donorService.findDonors(city, blankBloodType);

        // Then
        assertThat(actualDonors).isNotNull();
        verify(donorRepository, times(1))
                .searchByCityAndBloodType(city, null);
    }
}
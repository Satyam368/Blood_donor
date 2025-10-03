package com.example.blood_backend.util;

import com.example.blood_backend.entity.Donor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Utility class for creating test data objects
 */
public class TestDataUtil {

    private static final String[] FIRST_NAMES = {
            "John", "Jane", "Michael", "Sarah", "David", "Emily", "Robert", "Lisa",
            "William", "Maria", "James", "Jennifer", "Richard", "Linda", "Thomas", "Patricia"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas"
    };

    private static final String[] CITIES = {
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
            "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville"
    };

    private static final String[] STATES = {
            "NY", "CA", "IL", "TX", "AZ", "PA", "FL", "WA", "OR", "NV", "CO", "MA"
    };

    private static final String[] BLOOD_TYPES = {
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    };

    private static final Random random = new Random();

    /**
     * Creates a donor with random data
     */
    public static Donor createRandomDonor() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String fullName = firstName + " " + lastName;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@email.com";
        String city = CITIES[random.nextInt(CITIES.length)];
        String state = STATES[random.nextInt(STATES.length)];
        String bloodType = BLOOD_TYPES[random.nextInt(BLOOD_TYPES.length)];

        return Donor.builder()
                .fullName(fullName)
                .email(email)
                .phone("+1" + String.format("%010d", random.nextLong(10000000000L)))
                .bloodType(bloodType)
                .dateOfBirth(LocalDate.of(1970 + random.nextInt(40), 1 + random.nextInt(12), 1 + random.nextInt(28)))
                .weight(50.0 + random.nextDouble() * 50)
                .medicalConditions(random.nextBoolean() ? "None" : "Allergic to aspirin")
                .lastDonation(random.nextBoolean() ? LocalDate.now().minusDays(random.nextInt(365)) : null)
                .address((100 + random.nextInt(900)) + " " + lastName + " St")
                .city(city)
                .state(state)
                .zipCode(String.format("%05d", random.nextInt(100000)))
                .emergencyContact(firstName + " Emergency")
                .emergencyPhone("+1" + String.format("%010d", random.nextLong(10000000000L)))
                .agreeToTerms(true)
                .availableForEmergency(random.nextBoolean())
                .build();
    }

    /**
     * Creates a donor with specific parameters
     */
    public static Donor createDonor(String fullName, String email, String bloodType, String city) {
        return Donor.builder()
                .fullName(fullName)
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

    /**
     * Creates a minimal donor with only required fields
     */
    public static Donor createMinimalDonor(String fullName, String email, String bloodType) {
        return Donor.builder()
                .fullName(fullName)
                .email(email)
                .bloodType(bloodType)
                .agreeToTerms(true)
                .build();
    }

    /**
     * Creates a list of donors with different blood types in the same city
     */
    public static List<Donor> createDonorsInCity(String city, int count) {
        return Arrays.asList(
                createDonor("John Doe", "john@email.com", "O+", city),
                createDonor("Jane Smith", "jane@email.com", "A+", city),
                createDonor("Bob Johnson", "bob@email.com", "B+", city),
                createDonor("Alice Brown", "alice@email.com", "AB+", city),
                createDonor("Charlie Wilson", "charlie@email.com", "O-", city)
        ).subList(0, Math.min(count, 5));
    }

    /**
     * Creates a list of donors with the same blood type in different cities
     */
    public static List<Donor> createDonorsWithBloodType(String bloodType, int count) {
        return Arrays.asList(
                createDonor("John Doe", "john@email.com", bloodType, "New York"),
                createDonor("Jane Smith", "jane@email.com", bloodType, "Los Angeles"),
                createDonor("Bob Johnson", "bob@email.com", bloodType, "Chicago"),
                createDonor("Alice Brown", "alice@email.com", bloodType, "Houston"),
                createDonor("Charlie Wilson", "charlie@email.com", bloodType, "Phoenix")
        ).subList(0, Math.min(count, 5));
    }

    /**
     * Creates a donor with invalid data for testing validation
     */
    public static Donor createInvalidDonor() {
        return Donor.builder()
                .fullName("") // Empty name
                .email("invalid-email") // Invalid email format
                .bloodType("Invalid") // Invalid blood type
                .weight(-10.0) // Negative weight
                .agreeToTerms(false) // Does not agree to terms
                .build();
    }

    /**
     * Creates a donor for testing edge cases
     */
    public static Donor createEdgeCaseDonor() {
        return Donor.builder()
                .fullName("Test Donor with Very Long Name That Exceeds Normal Limits")
                .email("very.long.email.address.for.testing.purposes@verylongdomainname.com")
                .phone("+1234567890123456789") // Very long phone number
                .bloodType("O +") // Blood type with space
                .dateOfBirth(LocalDate.of(1900, 1, 1)) // Very old date
                .weight(999.99) // Very high weight
                .medicalConditions("Multiple conditions including diabetes, hypertension, and allergies to various medications")
                .address("Very Long Address Line That Contains Multiple Words And Special Characters !@#$%")
                .city("St. John's") // City with apostrophe
                .state("NL") // Non-US state
                .zipCode("A1A 1A1") // Canadian postal code format
                .emergencyContact("Emergency Contact with Special Characters !@#$%")
                .emergencyPhone("+1 (555) 123-4567 ext. 123") // Phone with formatting
                .agreeToTerms(true)
                .availableForEmergency(true)
                .build();
    }
}
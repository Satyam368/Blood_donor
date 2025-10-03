package com.example.blood_backend.performance;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.repository.DonorRepository;
import com.example.blood_backend.service.DonorService;
import com.example.blood_backend.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Blood Backend Performance Tests")
class BloodBackendPerformanceTest {

    @Autowired
    private DonorService donorService;

    @Autowired
    private DonorRepository donorRepository;

    @BeforeEach
    void setUp() {
        donorRepository.deleteAll();
    }

    @Test
    @DisplayName("Should handle large number of donor registrations efficiently")
    void bulkDonorRegistration_ShouldBeEfficient() {
        // Given
        int numberOfDonors = 1000;
        List<Donor> donors = new ArrayList<>();
        
        for (int i = 0; i < numberOfDonors; i++) {
            donors.add(TestDataUtil.createRandomDonor());
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // When
        for (Donor donor : donors) {
            donorService.saveDonor(donor);
        }

        stopWatch.stop();

        // Then
        long executionTime = stopWatch.getTotalTimeMillis();
        System.out.println("Bulk registration of " + numberOfDonors + " donors took: " + executionTime + " ms");
        
        // Should complete within reasonable time (adjust threshold as needed)
        assertThat(executionTime).isLessThan(10000); // 10 seconds
        assertThat(donorRepository.count()).isEqualTo(numberOfDonors);
    }

    @Test
    @DisplayName("Should handle concurrent donor registrations")
    void concurrentDonorRegistration_ShouldWork() throws Exception {
        // Given
        int numberOfThreads = 10;
        int donorsPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < donorsPerThread; j++) {
                    Donor donor = TestDataUtil.createRandomDonor();
                    donorService.saveDonor(donor);
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all threads to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        stopWatch.stop();

        // Then
        long executionTime = stopWatch.getTotalTimeMillis();
        int totalDonors = numberOfThreads * donorsPerThread;
        
        System.out.println("Concurrent registration of " + totalDonors + " donors (" + 
                          numberOfThreads + " threads) took: " + executionTime + " ms");
        
        assertThat(executionTime).isLessThan(15000); // 15 seconds
        assertThat(donorRepository.count()).isEqualTo(totalDonors);
        
        executor.shutdown();
    }

    @Test
    @DisplayName("Should search through large dataset efficiently")
    void searchLargeDataset_ShouldBeEfficient() {
        // Given - Create a large dataset
        int numberOfDonors = 5000;
        List<Donor> donors = new ArrayList<>();
        
        // Create donors with specific patterns for testing
        for (int i = 0; i < numberOfDonors; i++) {
            Donor donor = TestDataUtil.createRandomDonor();
            // Ensure some donors have "New York" as city and "O+" as blood type
            if (i % 10 == 0) {
                donor.setCity("New York");
                donor.setBloodType("O+");
            }
            donors.add(donor);
            donorService.saveDonor(donor);
        }

        // When & Then - Test various search scenarios
        StopWatch stopWatch = new StopWatch();

        // Test 1: Search by city
        stopWatch.start("citySearch");
        List<Donor> cityResults = donorService.findDonors("New York", null);
        stopWatch.stop();
        
        assertThat(cityResults).isNotEmpty();
        assertThat(cityResults.size()).isGreaterThan(numberOfDonors / 20); // Should find reasonable number
        
        // Test 2: Search by blood type
        stopWatch.start("bloodTypeSearch");
        List<Donor> bloodTypeResults = donorService.findDonors(null, "O+");
        stopWatch.stop();
        
        assertThat(bloodTypeResults).isNotEmpty();
        
        // Test 3: Search by both city and blood type
        stopWatch.start("combinedSearch");
        List<Donor> combinedResults = donorService.findDonors("New York", "O+");
        stopWatch.stop();
        
        assertThat(combinedResults).isNotEmpty();
        
        // Test 4: Get all donors
        stopWatch.start("getAllDonors");
        List<Donor> allDonors = donorService.getAllDonors();
        stopWatch.stop();
        
        assertThat(allDonors).hasSize(numberOfDonors);

        System.out.println("Search performance results:");
        System.out.println("City search: " + stopWatch.getTaskInfo()[0].getTimeMillis() + " ms");
        System.out.println("Blood type search: " + stopWatch.getTaskInfo()[1].getTimeMillis() + " ms");
        System.out.println("Combined search: " + stopWatch.getTaskInfo()[2].getTimeMillis() + " ms");
        System.out.println("Get all donors: " + stopWatch.getTaskInfo()[3].getTimeMillis() + " ms");
        
        // All searches should complete within reasonable time
        for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
            assertThat(taskInfo.getTimeMillis()).isLessThan(2000); // 2 seconds per search
        }
    }

    @Test
    @DisplayName("Should handle repository operations efficiently")
    void repositoryOperations_ShouldBeEfficient() {
        // Given
        int batchSize = 100;
        List<Donor> donors = new ArrayList<>();
        
        for (int i = 0; i < batchSize; i++) {
            donors.add(TestDataUtil.createRandomDonor());
        }

        StopWatch stopWatch = new StopWatch();

        // Test batch save
        stopWatch.start("batchSave");
        donorRepository.saveAll(donors);
        stopWatch.stop();

        // Test find all
        stopWatch.start("findAll");
        List<Donor> allDonors = donorRepository.findAll();
        stopWatch.stop();

        // Test custom search query
        stopWatch.start("customSearch");
        List<Donor> searchResults = donorRepository.searchByCityAndBloodType("New York", "O+");
        stopWatch.stop();
        assertThat(searchResults).isNotNull(); // Use the variable to avoid warning

        // Test count
        stopWatch.start("count");
        long count = donorRepository.count();
        stopWatch.stop();

        System.out.println("Repository operation performance:");
        System.out.println("Batch save (" + batchSize + " donors): " + stopWatch.getTaskInfo()[0].getTimeMillis() + " ms");
        System.out.println("Find all: " + stopWatch.getTaskInfo()[1].getTimeMillis() + " ms");
        System.out.println("Custom search: " + stopWatch.getTaskInfo()[2].getTimeMillis() + " ms");
        System.out.println("Count: " + stopWatch.getTaskInfo()[3].getTimeMillis() + " ms");

        // Verify results
        assertThat(allDonors).hasSize(batchSize);
        assertThat(count).isEqualTo(batchSize);
        
        // All operations should be fast
        for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
            assertThat(taskInfo.getTimeMillis()).isLessThan(1000); // 1 second per operation
        }
    }

    @Test
    @DisplayName("Should maintain performance with varied search patterns")
    void variedSearchPatterns_ShouldMaintainPerformance() {
        // Given - Setup diverse test data
        setupDiverseTestData();
        
        StopWatch stopWatch = new StopWatch();
        
        // Test various search patterns
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] bloodTypes = {"O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-"};
        
        stopWatch.start("variedSearches");
        
        for (String city : cities) {
            donorService.findDonors(city, null);
        }
        
        for (String bloodType : bloodTypes) {
            donorService.findDonors(null, bloodType);
        }
        
        for (String city : cities) {
            for (String bloodType : bloodTypes) {
                donorService.findDonors(city, bloodType);
            }
        }
        
        stopWatch.stop();
        
        long totalTime = stopWatch.getTotalTimeMillis();
        int totalSearches = cities.length + bloodTypes.length + (cities.length * bloodTypes.length);
        double averageTimePerSearch = (double) totalTime / totalSearches;
        
        System.out.println("Varied search patterns performance:");
        System.out.println("Total searches: " + totalSearches);
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average time per search: " + String.format("%.2f", averageTimePerSearch) + " ms");
        
        // Average search time should be reasonable
        assertThat(averageTimePerSearch).isLessThan(100); // 100ms average per search
    }

    private void setupDiverseTestData() {
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] bloodTypes = {"O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-"};
        
        // Create donors with all combinations of cities and blood types
        for (String city : cities) {
            for (String bloodType : bloodTypes) {
                for (int i = 0; i < 10; i++) { // 10 donors per combination
                    Donor donor = TestDataUtil.createDonor(
                            "Donor " + city + " " + bloodType + " " + i,
                            "donor." + city.replace(" ", "").toLowerCase() + "." + 
                            bloodType.replace("+", "plus").replace("-", "minus") + "." + i + "@email.com",
                            bloodType,
                            city
                    );
                    donorService.saveDonor(donor);
                }
            }
        }
    }
}
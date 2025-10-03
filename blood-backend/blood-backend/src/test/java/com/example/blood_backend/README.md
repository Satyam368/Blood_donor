# Blood Backend Testing Documentation

This document describes the comprehensive testing suite for the Blood Backend application.

## Test Structure

The testing suite is organized into several categories:

### 1. Unit Tests

#### Entity Tests
- **DonorTest** - Tests the Donor entity
  - Builder pattern functionality
  - Getters and setters
  - Equals and hashCode methods
  - ToString method
  - Null value handling

#### Service Tests
- **DonorServiceTest** - Tests the DonorService business logic
  - Saving donors
  - Retrieving all donors
  - Finding donors by city and blood type
  - Parameter validation and null handling
  - Uses Mockito for repository mocking

### 2. Integration Tests

#### Repository Tests
- **DonorRepositoryTest** - Tests the DonorRepository data access layer
  - Database operations (save, find, search)
  - Custom query testing (searchByCityAndBloodType)
  - Case-insensitive search functionality
  - Partial matching capabilities
  - Blood type normalization (handling spaces)
  - Uses H2 in-memory database

### 3. Web Layer Tests

#### Controller Tests
- **DonorControllerTest** - Tests the DonorController REST endpoints
  - POST /api/donors (donor registration)
  - GET /api/donors (get all donors)
  - JSON serialization/deserialization
  - CORS configuration
  - Error handling

- **FindDonorControllerTest** - Tests the FindDonorController search endpoints
  - GET /api/find-donors/search (search donors)
  - Query parameter handling
  - Search result validation
  - Edge case handling

### 4. End-to-End Tests

#### Integration Tests
- **BloodBackendIntegrationTest** - Tests the complete application flow
  - Full donor registration and search workflow
  - Multiple donor scenarios
  - Case-insensitive search validation
  - Blood type normalization testing
  - Data persistence verification

### 5. Performance Tests

#### Performance Tests
- **BloodBackendPerformanceTest** - Tests application performance and scalability
  - Bulk donor registration performance
  - Concurrent operation handling
  - Large dataset search efficiency
  - Repository operation benchmarks
  - Varied search pattern performance

### 6. Utility Classes

#### Test Utilities
- **TestDataUtil** - Utility class for generating test data
  - Random donor generation
  - Specific scenario donor creation
  - Edge case data generation
  - Bulk test data creation

#### Test Configuration
- **TestSecurityConfig** - Test-specific security configuration
  - Disables security for easier testing
  - Permits all requests in test environment

## Test Database Configuration

Tests use H2 in-memory database with the following configuration:
- Database URL: `jdbc:h2:mem:testdb`
- Hibernate DDL: `create-drop`
- SQL logging enabled for debugging

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Categories

#### Unit Tests Only
```bash
mvn test -Dtest="*Test"
```

#### Integration Tests Only
```bash
mvn test -Dtest="*IntegrationTest"
```

#### Performance Tests Only
```bash
mvn test -Dtest="*PerformanceTest"
```

#### Specific Test Class
```bash
mvn test -Dtest="DonorServiceTest"
```

#### Specific Test Method
```bash
mvn test -Dtest="DonorServiceTest#saveDonor_ShouldReturnSavedDonor"
```

## Test Coverage

The testing suite covers:

- ✅ Entity validation and behavior
- ✅ Service layer business logic
- ✅ Repository data access operations
- ✅ REST API endpoints
- ✅ JSON serialization/deserialization
- ✅ Database queries and custom searches
- ✅ Error handling and edge cases
- ✅ CORS configuration
- ✅ Performance and scalability
- ✅ Concurrent operations
- ✅ End-to-end workflows

## Test Data

Tests use a variety of test data including:
- Valid donor records with all fields
- Minimal donor records with required fields only
- Edge case data (special characters, long names, etc.)
- Invalid data for error testing
- Large datasets for performance testing
- Random data for bulk operations

## Assertions and Validation

Tests use AssertJ for fluent assertions providing:
- Readable test code
- Detailed error messages
- Rich assertion methods
- Collection and object assertions

## Mocking Strategy

- **Unit Tests**: Use Mockito to mock dependencies
- **Integration Tests**: Use real database (H2)
- **Web Layer Tests**: Use MockMvc with mocked services
- **End-to-End Tests**: Use real application context

## Best Practices Implemented

1. **Test Naming**: Descriptive test method names following Given-When-Then pattern
2. **Test Organization**: Logical grouping by functionality
3. **Test Data**: Isolated test data using @BeforeEach setup
4. **Database State**: Clean database state between tests using @Transactional
5. **Performance Monitoring**: Performance assertions with reasonable thresholds
6. **Documentation**: Clear test descriptions with @DisplayName annotations

## Continuous Integration

Tests are designed to run in CI environments with:
- No external dependencies
- In-memory database
- Reasonable execution times
- Reliable assertions

## Troubleshooting

### Common Issues

1. **Test Database Issues**: Ensure H2 dependency is in test scope
2. **Security Issues**: Use TestSecurityConfig for test profile
3. **Performance Test Failures**: Adjust thresholds based on CI environment
4. **Concurrent Test Issues**: Tests use @Transactional for isolation

### Debug Configuration

Enable debug logging in test profile:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
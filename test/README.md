# Movie Name Game - Testing Documentation

## Overview
This document outlines the testing strategy and implementation for the Movie Name Game project. The testing framework is designed to ensure robustness, correctness, and performance of all game components.

## Test Structure
The test suite is organized to mirror the main application structure, with test classes corresponding to each production class:

```
src/
├── test/
│   ├── model/
│   │   ├── MovieTest.java
│   │   ├── PlayerTest.java
│   │   └── MovieDatabaseTest.java
│   ├── controller/
│   │   ├── GameControllerTest.java
│   │   ├── ConnectionValidatorTest.java
│   │   └── TimerTest.java
│   ├── view/
│   │   └── ViewTest.java
│   └── integration/
│       ├── GameFlowTest.java
│       └── PerformanceTest.java
```

## Testing Framework
The project uses JUnit 5 as the primary testing framework, with the following additional tools:
- Mockito for mocking external dependencies
- JaCoCo for code coverage analysis
- JMH for performance benchmarking

## Test Categories

### Unit Tests
Individual components are tested in isolation to verify correct behavior:

1. **MovieTest.java**
   - Tests movie attribute storage
   - Validates connection detection between movies
   - Ensures proper handling of edge cases (null values, special characters)

2. **PlayerTest.java**
   - Verifies win condition tracking
   - Tests movie history management
   - Confirms genre counting logic

3. **MovieDatabaseTest.java**
   - Checks database loading and initialization
   - Tests movie retrieval performance
   - Validates autocomplete functionality

4. **GameControllerTest.java**
   - Tests turn management
   - Verifies move validation logic
   - Confirms win detection

5. **ConnectionValidatorTest.java**
   - Tests various connection types (actors, directors, etc.)
   - Validates edge cases and non-obvious connections
   - Ensures performance for large cast lists

6. **TimerTest.java**
   - Confirms accurate timing
   - Tests timeout callbacks
   - Verifies thread safety

7. **ViewTest.java**
   - Tests UI rendering
   - Validates input handling
   - Confirms output formatting

### Integration Tests
Tests how components work together:

1. **GameFlowTest.java**
   - Simulates complete game scenarios
   - Tests interaction between controllers and models
   - Validates state transitions through gameplay

2. **PerformanceTest.java**
   - Measures response times under load
   - Tests with large movie datasets
   - Validates memory usage

### Data Tests
Ensures the movie database is properly functioning:

1. **MovieDataValidityTest.java**
   - Validates CSV parsing
   - Tests API integration
   - Checks for data consistency

## Test Data
The test suite includes:

1. **Mock Movie Database**
   - A smaller subset (~1000 movies) of the main database
   - Includes edge cases and unusual movie metadata
   - Configured for fast test execution

2. **Sample Game Scenarios**
   - Predefined sequences of moves
   - Special scenarios testing edge cases
   - Win and loss condition examples

## Running Tests

### Prerequisites
- JDK 8 or higher
- Maven or Gradle

### Commands

#### Run All Tests
```bash
./mvnw test
```

#### Run Unit Tests Only
```bash
./mvnw test -Dtest="*Test"
```

#### Run Integration Tests
```bash
./mvnw test -Dtest="*IT"
```

#### Generate Coverage Report
```bash
./mvnw verify
```
Coverage reports will be available in `target/site/jacoco/index.html`

## Performance Benchmarks
Performance tests validate that the system meets response time requirements:

1. **Autocomplete Response**: < 50ms for suggestions
2. **Move Validation**: < 100ms to validate connections
3. **Database Lookups**: < 10ms for exact title matches

## Continuous Integration
Tests are automatically executed in the CI pipeline (GitHub Actions):
- On all pull requests to main branch
- On direct commits to main branch
- Nightly builds against latest movie data

## Writing New Tests
When adding new functionality:

1. Create corresponding test class if it doesn't exist
2. Follow AAA pattern (Arrange, Act, Assert)
3. Test both normal operation and edge cases
4. Ensure at least 80% code coverage for new code

## Common Testing Utilities

### MockMovieBuilder
```java
// Example usage
Movie testMovie = new MockMovieBuilder()
    .withTitle("Inception")
    .withDirector("Christopher Nolan")
    .withActor("Leonardo DiCaprio")
    .build();
```

### GameStateHelper
```java
// Example usage
GameState testState = GameStateHelper.createGameWithTwoPlayers();
GameStateHelper.advanceToSecondTurn(testState);
```

## Troubleshooting Tests
Common issues and solutions:

1. **Timing-Sensitive Tests**
   - Use `Awaitility` library for asynchronous tests
   - Avoid fixed Thread.sleep() calls

2. **Resource Leaks**
   - Use `@TearDown` to properly close resources
   - Monitor memory usage during large tests

3. **Flaky Tests**
   - Use deterministic random seeds for reproducibility
   - Isolate test state to prevent cross-contamination

## Test Logging
Test logs are written to `logs/test.log` with the following levels:
- ERROR: Test failures and exceptions
- WARN: Potential issues or unexpected behavior
- INFO: Test execution progress
- DEBUG: Detailed debugging information

## Responsible Team Member
- **Erlang Long**: Testing Lead
  - Implements core test infrastructure
  - Reviews test PRs
  - Maintains test documentation

## Future Testing Improvements
- Property-based testing with jqwik
- Mutation testing with PITest
- Expanded performance testing suite
- End-to-end testing for multiplayer functionality
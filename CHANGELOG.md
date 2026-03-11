# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Milestone 5: JSON File Persistence

#### JSON Persistence Implementation
- **Jackson Library**: Switched from Gson to Jackson for JSON serialization/deserialization
- **File-based Storage**: Transactions now persisted to `data/transactions.json`
- **JSON Repositories**: Three new repository implementations:
  - `JsonTransactionRepository` - Transaction persistence
  - `JsonCategoryRepository` - Category persistence  
  - `JsonBudgetRepository` - Budget persistence
- **Custom Serialization**: Handles Java time types (LocalDate, LocalDateTime) with custom formatters
- **Automatic Loading**: Transactions loaded from file on application startup
- **Real-time Saving**: All changes automatically saved to JSON file
- **Thread Safety**: All repository methods synchronized for concurrent access
- **Graceful Fallback**: Falls back to in-memory storage if JSON fails
- **Maven Shade Plugin**: Creates executable uber JAR with all dependencies
- **Jackson Annotations**: Model classes enhanced with `@JsonCreator` for deserialization
- **Comprehensive Testing**: 47 new tests for JSON repositories (47/47 passing)
- **Test Coverage**: 138 total tests with 100% coverage

#### Utility Classes
- **FileUtil**: File operations (read/write strings, directory creation, file management)
- **JacksonJsonUtil**: JSON serialization/deserialization with custom type handlers

#### Application Enhancements
- **App.java**: Modified to use JSON persistence by default
- **Data Directory**: Auto-creates `data/` directory for storage
- **User Feedback**: Shows data file location on startup
- **Error Handling**: Graceful degradation to in-memory storage
- **Executable JAR**: Uber JAR (2.3MB) includes all dependencies

#### Build Configuration
- **Jackson Dependencies**: Added jackson-databind, jackson-annotations, jackson-core, jackson-datatype-jsr310
- **Maven Shade Plugin**: Creates fat JAR for easy distribution
- **Uber JAR Size**: 2.3MB with all dependencies included
- **Maven Wrapper**: Added mvnw scripts for consistent Maven version across environments

#### Build Tool Improvements
- **Maven Wrapper**: Added wrapper scripts to eliminate Maven installation requirement
- **Wrapper Files**: `.mvn/wrapper/` with `maven-wrapper.properties`, `mvnw` (Unix), `mvnw.cmd` (Windows)
- **Version Lock**: Ensures Maven 3.9.12 is used consistently across all builds

#### Technical Implementation
- **Jackson 2.15.2**: Latest stable version for JSON processing
- **Java Time Module**: Proper serialization of LocalDate/LocalDateTime
- **JSON Format**: Human-readable with proper nesting for embedded objects
- **Data Integrity**: Atomic writes, proper exception handling

#### Testing Coverage
- **138 Total Tests**: All passing (0 failures)
- **JsonTransactionRepositoryTest**: 15 tests covering file I/O, persistence, CRUD, edge cases
- **JsonCategoryRepositoryTest**: 16 tests for category persistence
- **JsonBudgetRepositoryTest**: 16 tests for budget persistence
- **100% Test Coverage**: All new code thoroughly tested

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain model classes
│   │   ├── service/                 # Business logic layer
│   │   ├── repository/              # Data access layer
│   │   ├── cli/                     # CLI interface (BudgetBuddyCLI)
│   │   ├── util/                    # Utility classes (FileUtil, JacksonJsonUtil)
│   │   └── App.java                 # Application entry point
│   └── src/test/java/com/budget/
│       ├── model/
│       ├── service/
│       ├── cli/
│       └── repository/
└── pom.xml
```

#### Future Roadmap (Updated)
- **Phase 6**: Budget Management with spending limits and alerts
- **Phase 7**: Advanced Reporting and data visualization
- **Phase 8**: CSV Import/Export for data portability

#### Git Status
- **Current Version**: 1.0.0
- **Build Status**: ✅ All tests passing (138/138)
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: Budget Management Implementation

## [1.0.0] - Author: Nnazirim Nwaogu - 2026-03-02

### Milestone 4: CLI Menu System Implementation

#### CLI Interface Implementation
- **Interactive Menu System**: Complete CLI with main menu, transaction management, filtering, and reporting
- **User Input Handling**: Comprehensive input validation with user-friendly error messages
- **Transaction Operations**: Add, view, filter, and generate reports for transactions
- **Advanced Filtering**: Date range, category, and type-based filtering capabilities
- **Reporting Features**: Income vs expense summary, monthly breakdown, category-wise reports
- **Data Validation**: Integrated validation with existing ValidationService

#### Application Enhancements
- **App.java Entry Point**: Enhanced main application class with CLI initialization
- **Package Structure**: New CLI package with organized components
- **Test Coverage**: Comprehensive test suite for CLI functionality
- **Error Handling**: Robust exception handling with user-friendly messages

#### Technical Implementation
- **Java 21**: Latest LTS features with proper source/target configuration
- **Maven Build**: Standard Maven project structure with test execution
- **JUnit 5.10.2**: Comprehensive testing framework with 100% test coverage
- **Immutable Domain Model**: All model classes are final with private final fields

#### Development Standards
- **Coding Standards**: Consistent naming conventions and code organization
- **Validation Patterns**: Comprehensive input validation with descriptive exceptions
- **Testing Guidelines**: TDD approach with Arrange-Act-Assert pattern
- **Documentation**: Complete project documentation and guidelines

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/                    # Maven project root
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain model classes
│   │   ├── service/                 # Business logic layer
│   │   ├── repository/              # Data access layer
│   │   ├── cli/                     # CLI interface implementation
│   │   └── App.java                 # Application entry point
│   └── src/test/java/com/budget/     # Unit tests
└── pom.xml                           # Maven build configuration
```

#### Future Roadmap (Planned)
- **Phase 5**: File Persistence with CSV-based storage
- **Phase 6**: Budget Management with spending limits and alerts
- **Phase 7**: Advanced Reporting and data visualization

#### Git Status
- **Current Version**: 0.4.0
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: File Persistence Implementation

## [0.4.0] - Author: Nnazirim Nwaogu - 2026-03-02

### Milestone 3: Repository Layer and Transaction Service

### Added
- ValidationService class with centralized validation methods
- Comprehensive validation for all model classes using ValidationService
- TransactionService class for CRUD operations and filtering
- TransactionRepository interface and InMemoryTransactionRepository implementation
- 45 new test methods in ValidationServiceTest
- 45 new test methods in TransactionServiceTest
- 18 new test methods in InMemoryTransactionRepositoryTest
- Maven build configuration with main class specification
- Executable JAR packaging with proper manifest
- Milestone 3 implementation plan document

### Changed
- Updated Transaction class to use ValidationService for all validation
- Updated Category class to use ValidationService for validation
- Updated Budget class to use ValidationService for validation
- Enhanced model validation patterns with centralized service
- Updated project structure to include service and repository layers
- Enhanced README with Milestone 3 features
- Updated CHANGELOG.md to reflect new architecture components

### Fixed
- Validation edge cases with comprehensive test coverage
- Thread safety in in-memory repository implementation
- Error handling in service layer operations

#### Service Layer Implementation
- **TransactionService**: Complete business logic with CRUD operations and advanced filtering
- **Constructor Injection**: Testable design with dependency injection pattern
- **Business Logic**: Advanced filtering, date range queries, category/type searches
- **Error Handling**: Comprehensive null checks and validation

#### Testing Coverage
- **91 Total Tests**: Comprehensive test suite covering all components
- **InMemoryTransactionRepositoryTest**: 10 tests for repository implementation
- **TransactionServiceTest**: 11 tests for service operations
- **ValidationServiceTest**: 46 tests for validation methods
- **100% Test Coverage**: All public methods tested with edge cases

#### Architecture Enhancements
- **Clean Architecture**: Enhanced layered architecture with service/repository layers
- **SOLID Principles**: Better separation of concerns and dependency inversion
- **Repository Pattern**: Decoupled data access from business logic
- **Dependency Injection**: Constructor-based injection for testability

#### Build Configuration
- **Maven Compiler**: Configured for Java 21 source and target
- **Surefire Plugin**: JUnit 5 test execution with comprehensive reporting
- **Package**: Executable JAR generation with version 1.0.0-SNAPSHOT
- **Main Class**: Proper manifest configuration for executable JAR

#### Technical Implementation
- **Java 21**: Latest LTS features with proper source/target configuration
- **Maven Build**: Standard Maven project structure with test execution
- **JUnit 5.10.2**: Comprehensive testing framework with 100% test coverage
- **Immutable Domain Model**: All model classes are final with private final fields

#### Development Standards
- **Coding Standards**: Consistent naming conventions and code organization
- **Validation Patterns**: Comprehensive input validation with descriptive exceptions
- **Testing Guidelines**: TDD approach with Arrange-Act-Assert pattern
- **Documentation**: Complete project documentation and guidelines

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/                    # Maven project root
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain model classes
│   │   ├── service/                 # Business logic layer
│   │   ├── repository/              # Data access layer
│   │   └── util/                    # Utility classes
│   └── src/test/java/com/budget/     # Unit tests
└── pom.xml                           # Maven build configuration
```

#### Future Roadmap (Planned)
- **Phase 4**: CLI Interface with interactive menu system
- **Phase 5**: File Persistence with CSV-based storage
- **Phase 6**: Budget Management with spending limits and alerts
- **Phase 7**: Advanced Reporting and data visualization

#### Git Status
- **Current Version**: 0.3.0
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: CLI Interface Implementation

## [0.3.0] - Author: Nnazirim Nwaogu - 2026-02-28

### Milestone 3: In-Memory Storage and Transaction Service

#### Repository Layer - In-Progress
- **TransactionRepository Interface**: Abstract data access layer with CRUD operations
- **InMemoryTransactionRepository**: Thread-safe in-memory implementation using HashMap
- **Thread Safety**: Synchronized methods for concurrent access

#### Service Layer - In-Progress
- **TransactionService**: Complete CRUD operations with filtering and search capabilities
- **Constructor Injection**: Testable design with dependency injection
- **Business Logic**: Advanced filtering, date range queries, category/type searches

#### Validation Enhancements
- **Centralized Validation**: ValidationService with comprehensive validation methods
- **Consistent Patterns**: Uniform validation across all model classes
- **Enhanced Error Handling**: Descriptive exceptions with field names
- **Null Safety**: Proper null checks and empty string validation

#### Testing Coverage
- **69 Total Tests**: Comprehensive test suite covering all components
- **ValidationServiceTest**: 45 tests for validation methods
- **TransactionServiceTest**: 45 tests for service operations
- **InMemoryTransactionRepositoryTest**: 18 tests for repository implementation
- **100% Test Coverage**: All public methods tested with edge cases

#### Build Configuration
- **Maven Compiler**: Configured for Java 21 source and target
- **Surefire Plugin**: JUnit 5 test execution with comprehensive reporting
- **Package**: Executable JAR generation with version 1.0.0-SNAPSHOT
- **Main Class**: Proper manifest configuration for executable JAR

#### Architecture Improvements
- **Clean Architecture**: Enhanced layered architecture with service/repository layers
- **SOLID Principles**: Better separation of concerns and dependency inversion
- **Repository Pattern**: Decoupled data access from business logic
- **Dependency Injection**: Constructor-based injection for testability

#### Technical Implementation
- **Java 21**: Latest LTS features with proper source/target configuration
- **Maven Build**: Standard Maven project structure with test execution
- **JUnit 5.10.2**: Comprehensive testing framework with 100% test coverage
- **Immutable Domain Model**: All model classes are final with private final fields

#### Model Classes
- **Transaction**: Enhanced with notes field and comprehensive validation
- **Category**: Improved with proper validation and toString implementation
- **Budget**: Updated with centralized validation and thread safety
- **TransactionType**: Enhanced with isValidType() method for string parsing

#### Development Standards
- **Coding Standards**: Consistent naming conventions and code organization
- **Validation Patterns**: Comprehensive input validation with descriptive exceptions
- **Testing Guidelines**: TDD approach with Arrange-Act-Assert pattern
- **Documentation**: Complete project documentation and guidelines

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/                    # Maven project root
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain model classes
│   │   ├── service/                 # Business logic layer
│   │   ├── repository/              # Data access layer
│   │   └── util/                    # Utility classes
│   └── src/test/java/com/budget/     # Unit tests
└── pom.xml                           # Maven build configuration
```

#### Future Roadmap (Planned)
- **Phase 4**: CLI Interface with interactive menu system
- **Phase 5**: File Persistence with CSV-based storage
- **Phase 6**: Budget Management with spending limits and alerts
- **Phase 7**: Advanced Reporting and data visualization

#### Git Status
- **Current Version**: 0.2.0
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: CLI Interface Implementation

## [0.2.0] - Author: Nnazirim Nwaogu - 2026-02-19

#### Core Features
- **Transaction Management**: Immutable transaction records with UUID, amount, date, category, and notes
- **Category System**: Enum-based categorization with expense/income distinction
- **Type Distinction**: TransactionType enum for INCOME/EXPENSE/TRANSFER classification
- **Validation Framework**: Comprehensive input validation with descriptive exceptions

#### Technical Implementation
- **Java 21**: Latest LTS features with proper source/target configuration
- **Maven Build**: Standard Maven project structure with test execution
- **JUnit 5.10.2**: Comprehensive testing framework with 100% test coverage
- **Immutable Domain Model**: All model classes are final with private final fields

#### Model Classes
- **Transaction**: Immutable record with validation for ID, description, amount, date, category, and notes
- **Category**: Enum-based categorization with name, description, and transaction type
- **TransactionType**: Enum with INCOME, EXPENSE, TRANSFER types and parsing utilities

#### Testing Coverage
- **CategoryTest**: 5 tests covering validation, equality, hashcode, and toString
- **TransactionTest**: 6 tests covering validation, equality, hashcode, toString, and notes
- **TransactionTypeTest**: 5 tests covering type validation and parsing

#### Build Configuration
- **Maven Compiler**: Configured for Java 21 source and target
- **Surefire Plugin**: JUnit 5 test execution
- **Package**: Executable JAR generation with version 1.0.0-SNAPSHOT

#### Development Standards
- **Coding Standards**: PascalCase for classes, camelCase for methods
- **Validation Patterns**: Comprehensive input validation with descriptive exceptions
- **Testing Guidelines**: TDD approach with Arrange-Act-Assert pattern
- **Documentation**: Comprehensive project documentation and guidelines

#### Architecture
- **Clean Architecture**: Model layer implementation with service/repository/ui layers planned
- **SOLID Principles**: Single responsibility, open/closed, Liskov substitution, interface segregation, dependency inversion
- **TDD Approach**: Test-driven development with comprehensive test coverage

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/                    # Maven project root
│   ├── src/main/java/com/budget/
│   │   └── model/                   # Domain model classes
│   └── src/test/java/com/budget/model/ # Unit tests
└── pom.xml                           # Maven build configuration
```

#### Future Roadmap (Planned)
- **Phase 2**: CLI Interface with interactive menu system
- **Phase 3**: File Persistence with CSV-based storage
- **Phase 4**: Budget Management with spending limits and alerts
- **Phase 5**: Advanced Reporting and data visualization

#### Git Status
- **Initial Version**: 0.1.0
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: Repository and Service Layer Implementation

## [0.0.0] - Author: Nnazirim Nwaogu - 2026-02-18

---

## Types of Changes

**Added** for new features.
**Changed** for changes in existing functionality.
**Deprecated** for soon-to-be removed features.
**Removed** for now removed features.
**Fixed** for any bug fixes.
**Security** in case of vulnerabilities.

## Versioning

This project follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Given a version number MAJOR.MINOR.PATCH, increment the:

1. MAJOR version when you make incompatible API changes,
2. MINOR version when you add functionality in a backwards compatible manner, and
3. PATCH version when you make backwards compatible bug fixes.

Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

## Release Schedule

- **Major releases**: Significant architectural changes or breaking changes
- **Minor releases**: New features and functionality
- **Patch releases**: Bug fixes and documentation updates

## Project Status

- **Development Stage**: 0.2.0
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: CLI Interface Implementation
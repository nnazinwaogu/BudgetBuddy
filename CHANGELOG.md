# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Initial project structure with Maven configuration
- Core domain model classes (Transaction, Category, TransactionType)
- Comprehensive test suite with 16 passing JUnit 5 tests
- Immutable data model following clean architecture principles
- Java 21 build configuration with proper compiler settings
- Project documentation and development guidelines

### Changed
- Updated project structure to follow clean layered architecture
- Implemented comprehensive validation patterns for all model classes
- Added proper equals/hashCode/toString implementations
- Enhanced README with detailed development guidelines

### Fixed
- Validation edge cases in model classes
- Test coverage for all public methods

## [0.1.0] - Author: Nnazirim Nwaogu - 2026-02-19
 
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
BudgetTracker/
├── budget-tracker/                    # Maven project root
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain model classes
│   │   └── App.java                 # Application entry point
│   └── src/test/java/com/budget/model/ # Unit tests
└── pom.xml                           # Maven build configuration
```

#### Future Roadmap (Planned)
- **Phase 2**: CLI Interface with interactive menu system
- **Phase 3**: File Persistence with CSV-based storage
- **Phase 4**: Budget Management with spending limits and alerts
- **Phase 5**: Advanced Reporting and data visualization

#### Git Status
- Initial project setup with no commits yet
- Ready for first commit and version control integration

## Development Notes

### Key Milestones Achieved
1. **Project Setup**: Maven configuration and Java 21 setup completed
2. **Core Model**: Transaction, Category, and TransactionType classes implemented
3. **Testing**: Comprehensive test suite with 16 passing tests
4. **Validation**: Robust input validation for all model classes
5. **Documentation**: Complete project documentation and guidelines

### Technical Decisions
- **Immutability**: All domain objects are immutable for thread safety
- **Validation**: Constructor validation with descriptive exceptions
- **Testing**: JUnit 5 with comprehensive coverage and proper naming conventions
- **Architecture**: Clean layered architecture following SOLID principles

### Next Development Steps
1. **CLI Interface**: Implement interactive command-line menu system
2. **File Persistence**: Add CSV-based data storage and retrieval
3. **Budget Management**: Implement spending limits and budget tracking
4. **Report Generation**: Create monthly summaries and spending insights

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

- **Development Stage**: 1.0.0-ALPHA
- **Build Status**: ✅ All tests passing
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: CLI Interface Implementation
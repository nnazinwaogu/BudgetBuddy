# BudgetTracker

A personal budget and expense tracking application built with clean architecture principles in Java 21.
Written by: Nnazirim Nwaogu

## 🚀 Overview

BudgetTracker helps you manage your personal finances by tracking income, expenses, and providing insights into your spending patterns. Built with modern Java practices, this application emphasizes immutability, comprehensive testing, and clean architecture.

## 📋 Current Features

### Core Model (Completed)
- **Transaction Management**: Create and manage financial transactions with validation
- **Category System**: Organize expenses and income into meaningful categories
- **Type Distinction**: Separate income from expenses for accurate tracking
- **Immutable Data Model**: Thread-safe, immutable domain objects
- **Comprehensive Testing**: 16 passing JUnit 5 tests with 100% coverage

### Service Layer (Milestone 2 Completed)
- **ValidationService**: Centralized validation for all model classes
- **TransactionService**: CRUD operations and transaction filtering
- **InMemoryTransactionRepository**: Thread-safe in-memory storage
- **Comprehensive Testing**: 69 passing JUnit 5 tests with 100% coverage

### Repository Layer (Milestone 3 Completed)
- **TransactionRepository Interface**: Complete CRUD operations with findById, findAll, save, delete, exists methods
- **InMemoryTransactionRepository**: Thread-safe implementation using HashMap and ArrayList
- **Thread Safety**: Synchronized methods for concurrent access patterns
- **Data Structures**: HashMap for O(1) lookups, ArrayList for ordered queries

### Milestone 3 Features
- **Repository Pattern**: Decoupled data access from business logic
- **Dependency Injection**: Constructor-based injection for testability
- **Business Logic**: Advanced filtering, date range queries, category/type searches
- **Error Handling**: Comprehensive null checks and validation

### CLI Interface (Milestone 4 Completed)
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
BudgetTracker/
├── budget-tracker/                    # Maven project root
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

## 🛠️ Technical Requirements

- **Java 21+** JDK
- **Maven 3.8+** for build management
- **Operating System**: Windows, macOS, or Linux

## 📦 Getting Started

### Prerequisites
Ensure you have Java 21 JDK and Maven installed on your system.

### Build and Test
```bash
# Clone the repository
git clone <repository-url>
cd budget-tracker

# Build the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package -Dmaven.test.skip=true
```

### Run the Application
```bash
# Execute the main class
java -cp target/budget-tracker-1.0.0-SNAPSHOT.jar com.budget.App
```

## 🏗️ Architecture

BudgetTracker follows a clean layered architecture with these completed layers:

```
src/main/java/com/budget/
├── model/           # Domain entities (completed)
├── service/         # Business logic (completed - Milestone 2)
├── repository/      # Data persistence (completed - Milestone 3)
├── cli/             # CLI interface (completed - Milestone 4)
└── App.java         # Application entry point
```
├── util/            # Helper utilities (planned)
└── ui/              # User interface (planned)
```

### Design Principles
- **Immutability**: All domain objects are immutable
- **SOLID Principles**: Clean, maintainable code
- **TDD Approach**: Comprehensive test coverage
- **Clean Architecture**: Separation of concerns

## 🔧 Development

### Coding Standards
- **Java 21**: Latest LTS features
- **Immutability**: All model classes are `final` with `private final` fields
- **Validation**: Constructor validation with descriptive exceptions
- **Naming**: PascalCase for classes, camelCase for methods

### Testing Guidelines
- **JUnit 5.10.2**: Comprehensive unit testing
- **Test Coverage**: All public methods must have tests
- **Naming Convention**: `testValidClassNameOperation()`
- **Patterns**: Arrange-Act-Assert structure

### Build Commands
```bash
# Compile
mvn compile

# Test
mvn test

# Package
mvn package

# Clean build
mvn clean package
```

## 📊 Current Implementation

### Domain Model
- **Transaction**: Immutable transaction record with UUID, amount, date, category, and notes
- **Category**: Enum-based categorization system (Food, Utilities, Entertainment, etc.)
- **TransactionType**: Enum distinguishing INCOME vs EXPENSE

### Validation Rules
- **Transaction ID**: Random UUID, non-empty
- **Description**: 1-200 characters, non-empty
- **Amount**: Must be greater than zero
- **Category**: Cannot be null
- **Notes**: Optional, max 500 characters

### Test Coverage
- **CategoryTest**: 5 tests for validation, equality, hashcode, and toString
- **TransactionTest**: 6 tests for validation, equality, hashcode, toString, and notes
- **TransactionTypeTest**: 5 tests for type validation and parsing
- **ValidationServiceTest**: 45 tests for validation methods
- **TransactionServiceTest**: 11 tests for service operations
- **InMemoryTransactionRepositoryTest**: 10 tests for repository implementation
- **BudgetTrackerCLITest**: 8 tests for CLI functionality

**Total Tests**: 91 passing JUnit 5 tests with 100% coverage

## 🚀 Roadmap

### Phase 1: Core Model (Completed)
- [x] Transaction entity with validation
- [x] Category system
- [x] TransactionType enum
- [x] Comprehensive test suite

### Phase 2: Service Layer (Completed)
- [x] ValidationService with centralized validation
- [x] TransactionService with CRUD operations
- [x] InMemoryTransactionRepository implementation
- [x] 69 passing JUnit 5 tests

### Phase 3: Repository Layer (Completed)
- [x] TransactionRepository interface
- [x] Thread-safe in-memory implementation
- [x] Repository pattern implementation
- [x] Dependency injection design

### Phase 4: CLI Interface (Completed)
- [x] Main menu system
- [x] Transaction input UI
- [x] Category management
- [x] Budget management
- [x] Report generation

### Phase 5: Persistence (Planned)
- [ ] File-based transaction storage
- [ ] Budget persistence
- [ ] Data import/export
- [ ] Backup and restore

### Phase 6: Advanced Features (Planned)
- [ ] Budget alerts and notifications
- [ ] Advanced reporting
- [ ] Data visualization
- [ ] Multi-currency support

## 🤝 Contributing

BudgetTracker welcomes contributions! Please follow these guidelines:

### Development Workflow
1. Fork the repository
2. Create a feature branch
3. Write tests first (TDD)
4. Implement functionality
5. Ensure all tests pass
6. Submit a pull request

### Code Requirements
- Follow existing coding standards
- Maintain 100% test coverage
- Use descriptive variable and method names
- Include Javadoc for public APIs
- Add validation for all user inputs

### Testing Requirements
- All new features must have corresponding tests
- Test boundary conditions and error cases
- Follow existing test naming conventions
- Ensure tests are independent and repeatable

## 📝 License

This project is open source and available under the MIT License.

## 📞 Support

For questions, issues, or feature requests, please:
1. Review existing issues
2. Create a new issue with detailed information
3. Include steps to reproduce any bugs

## 🎯 Project Status

- **Version History**: [0.4.0](CHANGELOG.md)
- **Core Model**: ✅ Complete
- **Tests**: ✅ All passing (91/91)
- **Service Layer**: ✅ Complete
- **Repository Layer**: ✅ Complete
- **CLI Interface**: ✅ Complete
- **Persistence**: 🔄 In planning
- **Documentation**: 📝 Comprehensive
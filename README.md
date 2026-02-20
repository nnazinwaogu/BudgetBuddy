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

### Planned Features
- **CLI Interface**: Interactive command-line menu system
- **File Persistence**: CSV-based data storage and retrieval
- **Budget Management**: Set spending limits and receive alerts
- **Report Generation**: Monthly summaries and spending insights
- **Data Export**: Export data in multiple formats

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

BudgetTracker follows a clean layered architecture with these planned layers:

```
src/main/java/com/budget/
├── model/           # Domain entities (completed)
├── service/         # Business logic (planned)
├── repository/      # Data persistence (planned)
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
- **Transaction ID**: Radnom UUId non-empty
- **Description**: 1-200 characters, non-empty
- **Amount**: Must be greater than zero
- **Category**: Cannot be null
- **Notes**: Optional, max 500 characters

### Test Coverage
- **CategoryTest**: 5 tests for validation, equality, hashcode, and toString
- **TransactionTest**: 6 tests for validation, equality, hashcode, toString, and notes
- **TransactionTypeTest**: 5 tests for type validation and parsing

## 🚀 Roadmap

### Phase 1: Core Model (Completed)
- [x] Transaction entity with validation
- [x] Category system
- [x] TransactionType enum
- [x] Comprehensive test suite

### Phase 2: CLI Interface (Planned)
- [ ] Main menu system
- [ ] Transaction input UI
- [ ] Category management
- [ ] Budget management
- [ ] Report generation

### Phase 3: Persistence (Planned)
- [ ] File-based transaction storage
- [ ] Budget persistence
- [ ] Data import/export
- [ ] Backup and restore

### Phase 4: Advanced Features (Planned)
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

- **Version History**: [1.0.0-ALPHA](CHANGELOG.md)
- **Core Model**: ✅ Complete
- **Tests**: ✅ All passing (18/18)
- **CLI Interface**: 🔄 In planning
- **Persistence**: 🔄 In planning
- **Documentation**: 📝 Comprehensive


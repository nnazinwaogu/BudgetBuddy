# BudgetBuddy

A personal budget and expense tracking application built with clean architecture principles in Java 21.
Written by: Nnazirim Nwaogu

## 🚀 Overview

BudgetBuddy helps you manage your personal finances by tracking income, expenses, and providing insights into your spending patterns. Built with modern Java practices, this application emphasizes immutability, comprehensive testing, and clean architecture.

## 📋 Current Features

### Core Model (Completed)
- **Transaction Management**: Create and manage financial transactions with validation
- **Category System**: Organize expenses and income into meaningful categories
- **Type Distinction**: Separate income from expenses for accurate tracking
- **Immutable Data Model**: Thread-safe, immutable domain objects
- **Comprehensive Testing**: 138 passing JUnit 5 tests with 100% coverage

### Service Layer (Completed)
- **ValidationService**: Centralized validation for all model classes
- **TransactionService**: CRUD operations and transaction filtering
- **Multiple Repository Implementations**: Both in-memory and JSON file-based storage
- **Advanced Filtering**: Date range, category, and type-based searches

### Repository Layer (Completed)
- **Repository Pattern**: Full implementation with interfaces and concrete classes
- **InMemoryTransactionRepository**: Thread-safe in-memory storage for testing
- **JsonTransactionRepository**: JSON file-based persistence with automatic save/load
- **JsonCategoryRepository**: JSON persistence for custom categories
- **JsonBudgetRepository**: JSON persistence for budget limits
- **Thread Safety**: All repository methods synchronized for concurrent access
- **Data Structures**: Efficient HashMap for O(1) lookups, ArrayList for ordered queries

### CLI Interface (Completed)
- **Interactive Menu System**: Complete CLI with main menu and sub-menus
- **Transaction Management**: Add, view, filter, and delete transactions
- **Advanced Filtering**: By date range, category, and transaction type
- **Reporting Features**:
  - Income vs Expense Summary with net balance
  - Monthly Expense Breakdown (grouped by month)
  - Category-wise Expense Report
- **User Input Validation**: Comprehensive validation with helpful error messages
- **Data Persistence**: All transactions automatically saved to JSON file
- **Error Handling**: Graceful fallback to in-memory storage if file operations fail

### JSON File Persistence (Milestone 5 - Completed)
- **Jackson Library**: Professional JSON processing with Jackson 2.15.2
- **Automatic File Management**: Creates `data/` directory and `transactions.json` file
- **Real-time Saving**: Every transaction immediately persisted to disk
- **Startup Loading**: Automatically loads previous transactions on app launch
- **Custom Serialization**: Proper handling of LocalDate, LocalDateTime, and nested objects
- **Human-Readable Format**: Clean JSON structure for debugging and manual editing
- **Data Integrity**: Atomic file operations with proper error handling
- **No Data Loss**: Graceful degradation ensures transactions never lost

#### Technical Implementation
- **Jackson Dependencies**: 
  - `jackson-databind` (2.15.2)
  - `jackson-annotations` (2.15.2)
  - `jackson-core` (2.15.2)
  - `jackson-datatype-jsr310` (2.15.2) for Java time types
- **Utility Classes**:
  - `FileUtil` - File I/O operations (read/write, directory management)
  - `JacksonJsonUtil` - JSON serialization with custom type adapters
- **Model Enhancements**: Added `@JsonCreator` constructors to all models for deserialization
- **Maven Shade Plugin**: Creates executable uber JAR (2.3MB) with all dependencies

#### Application Enhancements
- **Smart Initialization**: Tries JSON first, falls back to in-memory on failure
- **User Feedback**: Shows data file location on startup
- **No Configuration Required**: Works out-of-the-box with sensible defaults
- **Cross-Platform**: Works on Windows, macOS, and Linux

#### Project Structure
```
BudgetBuddy/
├── budget-buddy/
│   ├── src/main/java/com/budget/
│   │   ├── model/                   # Domain entities (Transaction, Category, TransactionType, Budget)
│   │   ├── service/                 # Business logic (ValidationService, TransactionService)
│   │   ├── repository/              # Data persistence (all repository implementations)
│   │   ├── cli/                     # CLI interface (BudgetBuddyCLI)
│   │   ├── util/                    # Utilities (FileUtil, JacksonJsonUtil)
│   │   └── App.java                 # Application entry point
│   └── src/test/java/com/budget/
│       ├── model/                   # Model tests
│       ├── service/                 # Service tests
│       ├── repository/              # Repository tests (including JSON tests)
│       └── cli/                     # CLI tests
└── pom.xml                          # Maven build configuration with all dependencies
```

#### Testing Coverage
- **138 Total Tests**: All passing with 100% coverage
- **Model Tests**: 26 tests (Transaction, Category, TransactionType, Budget)
- **Service Tests**: 57 tests (ValidationService, TransactionService)
- **Repository Tests**: 47 tests (InMemory + JSON implementations)
- **CLI Tests**: 8 tests (user interface validation)
- **JSON Repository Tests**: 46 new tests specifically for file persistence

#### Git Status
- **Current Version**: 1.0.0
- **Build Status**: ✅ All tests passing (138/138)
- **Code Coverage**: 100%
- **Documentation**: Comprehensive
- **Next Milestone**: Budget Management Implementation

## 🛠️ Technical Requirements

- **Java 21+** JDK
- **Maven 3.8+** for build management
- **Operating System**: Windows, macOS, or Linux

## 📦 Getting Started

### Prerequisites
Ensure you have **Java 21 JDK** installed. Maven is **not required** - the Maven wrapper (`mvnw`) will automatically download the correct Maven version.

### Build and Test
```bash
# Clone the repository
git clone <repository-url>
cd budget-buddy

# Build the project using Maven wrapper
./mvnw clean compile

# Run tests using Maven wrapper
./mvnw test

# Package the application (skips tests)
./mvnw package -Dmaven.test.skip=true
```
*Note: Windows users should use `mvnw.cmd` instead of `./mvnw`.*

### Run the Application
```bash
# Execute the main class
java -cp target/budget-buddy-1.0.0-SNAPSHOT.jar com.budget.App
```

## 🏗️ Architecture

BudgetBuddy follows a clean layered architecture with these completed layers:

```
src/main/java/com/budget/
├── model/           # Domain entities (completed)
├── service/         # Business logic (completed - Milestone 2)
├── repository/      # Data persistence (completed - Milestone 3)
├── cli/             # CLI interface (completed - Milestone 4)
└── App.java         # Application entry point
```
├── util/            # Helper utilities (completed - Milestone 5)
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

## 🚀 Roadmap

### Phase 1: Core Model (✅ Completed)
- [x] Transaction entity with validation
- [x] Category system with dynamic creation
- [x] TransactionType enum (INCOME, EXPENSE, TRANSFER)
- [x] Budget entity with monthly limits
- [x] Comprehensive test suite

### Phase 2: Service Layer (✅ Completed)
- [x] ValidationService with centralized validation
- [x] TransactionService with CRUD operations
- [x] Advanced filtering (date range, category, type)
- [x] Comprehensive test coverage

### Phase 3: Repository Layer (✅ Completed)
- [x] Repository interfaces (Transaction, Category, Budget)
- [x] InMemory implementations for all repositories
- [x] JSON file-based implementations for all repositories
- [x] Thread-safe synchronized methods
- [x] Repository pattern with dependency injection
- [x] 100% test coverage for all repository implementations

### Phase 4: CLI Interface (✅ Completed)
- [x] Interactive menu system with navigation
- [x] Transaction input with validation
- [x] View all transactions with formatted display
- [x] Advanced filtering options (date, category, type)
- [x] Reporting: Income/Expense summary, monthly breakdown, category reports
- [x] User-friendly error handling and input validation
- [x] Comprehensive CLI test coverage

### Phase 5: JSON File Persistence (✅ Completed)
- [x] Jackson library integration (2.15.2)
- [x] JsonTransactionRepository with auto-save/load
- [x] JsonCategoryRepository for category persistence
- [x] JsonBudgetRepository for budget persistence
- [x] Utility classes: FileUtil, JacksonJsonUtil
- [x] Custom LocalDate/LocalDateTime serialization
- [x] Automatic data directory creation
- [x] Graceful fallback to in-memory storage
- [x] Maven Shade plugin for uber JAR packaging
- [x] 47 new repository tests (138 total tests)
- [x] 100% code coverage maintained
- [x] Production-ready executable JAR

### Phase 6: Budget Management (Next Milestone)
- [ ] BudgetService for budget tracking and alerts
- [ ] Budget UI integration in CLI
- [ ] Spending vs budget comparison
- [ ] Budget status display (within/over limits)
- [ ] Budget configuration and editing
- [ ] Over-budget warnings (>80% threshold)

### Phase 7: Advanced Reporting (Planned)
- [ ] CSV export for transactions
- [ ] Enhanced monthly reports with charts (ASCII)
- [ ] Year-over-year comparisons
- [ ] Category trend analysis
- [ ] Export filtered data to custom files

### Phase 8: Quality & Polish (Planned)
- [ ] Logging framework integration (SLF4J/Logback)
- [ ] Configuration file support
- [ ] Backup and restore functionality
- [ ] Data validation and repair tools
- [ ] Performance optimizations
- [ ] User documentation and help system

## 🤝 Contributing

BudgetBuddy welcomes contributions! Please follow these guidelines:

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

- **Version History**: [1.0.0](CHANGELOG.md)
- **Core Model**: ✅ Complete
- **Tests**: ✅ All passing (138/138)
- **Service Layer**: ✅ Complete
- **Repository Layer**: ✅ Complete (JSON + InMemory)
- **CLI Interface**: ✅ Complete
- **Persistence**: ✅ Complete (JSON file-based with fallback)
- **Documentation**: 📝 Comprehensive
- **Next Milestone**: Budget Management (Phase 6)
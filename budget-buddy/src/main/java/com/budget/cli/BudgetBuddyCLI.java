package com.budget.cli;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.budget.model.Transaction;
import com.budget.model.Category;
import com.budget.model.TransactionType;
import com.budget.service.TransactionService;
import com.budget.service.ValidationService;

public class BudgetBuddyCLI {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final TransactionService transactionService;
    
    public BudgetBuddyCLI(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    public void start() {
        System.out.println("=== BudgetBuddy CLI ===");
        System.out.println("Personal Budget & Expense Management System");
        System.out.println("==============================");
        
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        addTransaction();
                        break;
                    case 2:
                        viewAllTransactions();
                        break;
                    case 3:
                        filterTransactions();
                        break;
                    case 4:
                        generateReport();
                        break;
                    case 5:
                        running = false;
                        System.out.println("Thank you for using BudgetBuddy!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                try {
                    scanner.nextLine();
                } catch (Exception ex) {
                    // Ignore if no input available
                }
            }
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Transaction");
        System.out.println("2. View All Transactions");
        System.out.println("3. Filter Transactions");
        System.out.println("4. Generate Report");
        System.out.println("5. Exit");
    }
    
    private void addTransaction() {
        System.out.println("\n=== Add Transaction ===");
        
        String description = readString("Description: ", 200);
        BigDecimal amount = readBigDecimal("Amount: ");
        LocalDate date = readDate("Date (yyyy-MM-dd): ");
        TransactionType type = readTransactionType();
        Category category = readCategory(type);
        String notes = readOptionalString("Notes (optional): ", 500);
        
        Category transactionCategory = new Category(category.getName(), category.getDescription(), type);
        Transaction transaction = new Transaction(description, amount, date, transactionCategory, notes);
        
        transactionService.addTransaction(transaction);
        System.out.println("Transaction added successfully!");
        System.out.println("Transaction ID: " + transaction.getId());
    }
    
    private void viewAllTransactions() {
        System.out.println("\n=== All Transactions ===");
        
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        for (Transaction transaction : transactions) {
            System.out.println(formatTransaction(transaction));
        }
        
        System.out.println("\nTotal transactions: " + transactions.size());
    }
    
    private void filterTransactions() {
        System.out.println("\n=== Filter Transactions ===");
        System.out.println("1. By Date Range");
        System.out.println("2. By Category");
        System.out.println("3. By Type");
        System.out.println("4. Go Back");
        
        int choice = readInt("Enter your choice: ");
        
        switch (choice) {
            case 1:
                filterByDateRange();
                break;
            case 2:
                filterByCategory();
                break;
            case 3:
                filterByType();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void filterByDateRange() {
        System.out.println("\n=== Filter by Date Range ===");
        LocalDate startDate = readDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("End date (yyyy-MM-dd): ");
        
        List<Transaction> transactions = transactionService.findTransactionsByDateRange(startDate, endDate);
        displayFilteredTransactions(transactions, "Transactions from " + startDate + " to " + endDate);
    }
    
    private void filterByCategory() {
        System.out.println("\n=== Filter by Category ===");
        TransactionType type = readTransactionType();
        Category category = readCategory(type);
        
        List<Transaction> transactions = transactionService.findTransactionsByCategory(category.getName());
        displayFilteredTransactions(transactions, "Transactions in category: " + category.getName());
    }
    
    private void filterByType() {
        System.out.println("\n=== Filter by Type ===");
        TransactionType type = readTransactionType();
        
        List<Transaction> transactions = transactionService.findTransactionsByType(type.name());
        displayFilteredTransactions(transactions, "Transactions of type: " + type.name());
    }
    
    private void generateReport() {
        System.out.println("\n=== Generate Report ===");
        System.out.println("1. Income vs Expense Summary");
        System.out.println("2. Monthly Expense Breakdown");
        System.out.println("3. Category-wise Expense Report");
        System.out.println("4. Go Back");
        
        int choice = readInt("Enter your choice: ");
        
        switch (choice) {
            case 1:
                generateIncomeExpenseSummary();
                break;
            case 2:
                generateMonthlyBreakdown();
                break;
            case 3:
                generateCategoryReport();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void generateIncomeExpenseSummary() {
        System.out.println("\n=== Income vs Expense Summary ===");
        
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        
        BigDecimal totalIncome = allTransactions.stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.INCOME)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalExpense = allTransactions.stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalTransfer = allTransactions.stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.TRANSFER)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expense: $" + totalExpense);
        System.out.println("Total Transfer: $" + totalTransfer);
        
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal netBalance = totalIncome.subtract(totalExpense);
            System.out.println("Net Balance: $" + netBalance);
            
            if (netBalance.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("You're in surplus!");
            } else if (netBalance.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("You're in deficit!");
            } else {
                System.out.println("Your income matches your expenses.");
            }
        }
    }
    
    private void generateMonthlyBreakdown() {
        System.out.println("\n=== Monthly Expense Breakdown ===");
        
        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());
            
        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }
        
        expenses.stream()
            .collect(Collectors.groupingBy(
                t -> t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue()),
                Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
            ))
            .forEach((month, total) -> {
                System.out.println(month + ": $" + total);
            });
    }
    
    private void generateCategoryReport() {
        System.out.println("\n=== Category-wise Expense Report ===");
        
        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());
            
        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }
        
        expenses.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCategory().getName(),
                Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
            ))
            .forEach((category, total) -> {
                System.out.println(category + ": $" + total);
            });
    }
    
    private void displayFilteredTransactions(List<Transaction> transactions, String title) {
        System.out.println("\n" + title + " ===");
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        for (Transaction transaction : transactions) {
            System.out.println(formatTransaction(transaction));
        }
        
        System.out.println("\nTotal transactions: " + transactions.size());
    }
    
    private String formatTransaction(Transaction transaction) {
        return String.format(
            "ID: %s | %s | %.2f | %s | %s | %s",
            transaction.getId(),
            transaction.getDescription(),
            transaction.getAmount(),
            DATE_FORMATTER.format(transaction.getDate()),
            transaction.getCategory().getName(),
            transaction.getCategory().getTransactionType()
        );
    }
    
    String readString(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                ValidationService.validateString(input, "Input", maxLength);
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    String readOptionalString(String prompt, int maxLength) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            ValidationService.validateLength(input, "Input", maxLength);
            return input;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return readOptionalString(prompt, maxLength);
        }
    }
    
    BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(input);
                ValidationService.validateGreaterThanZero(amount, "Amount");
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                ValidationService.validateTransactionDate(date);
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter date in format yyyy-MM-dd.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NoSuchElementException e) {
                System.out.println("\nError: No input available. Please run the application from a terminal with input capabilities.");
                System.exit(1);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }
    
    TransactionType readTransactionType() {
        while (true) {
            System.out.println("\nSelect transaction type:");
            System.out.println("1. Income");
            System.out.println("2. Expense");
            System.out.println("3. Transfer");
            
            int choice = readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    return TransactionType.INCOME;
                case 2:
                    return TransactionType.EXPENSE;
                case 3:
                    return TransactionType.TRANSFER;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    Category readCategory(TransactionType type) {
        while (true) {
            System.out.print("Category name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Category description: ");
            String description = scanner.nextLine().trim();
            
            try {
                ValidationService.validateCategoryName(name);
                ValidationService.validateCategoryDescription(description);
                
                return new Category(name, description, type);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
package com.budget.service;

import java.math.BigDecimal;
import java.util.Objects;

import com.budget.model.Category;
import com.budget.model.TransactionType;

/**
 * Centralized validation service for the BudgetTracker application.
 * Provides static methods for validating input data across all model classes.
 * Ensures consistent error handling and validation patterns throughout the application.
 */
public final class ValidationService {
    
    private ValidationService() {
        // Prevent instantiation
    }
    
    /**
     * Validates that an object is not null.
     * 
     * @param value the object to validate
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the value is null
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
    
    /**
     * Validates that a string is not null and not empty.
     * 
     * @param value the string to validate
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
    
    /**
     * Validates that two objects are equal.
     * @param value the first object to compare
     * @param expectedValue the second object to compare
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the objects are not equal
     */
    public static void validateEqual(Object value, Object expectedValue, String fieldName) {
        if (!Objects.equals(value, expectedValue)) {
            throw new IllegalArgumentException(fieldName + " must be equal to " + expectedValue);
        }
    }

    /**
     * Validates that a string does not exceed the specified maximum length.
     * 
     * @param value the string to validate
     * @param fieldName the name of the field being validated
     * @param maxLength the maximum allowed length
     * @throws IllegalArgumentException if the string exceeds the maximum length
     */
    public static void validateLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " cannot exceed " + maxLength + " characters");
        }
    }
    
    /**
     * Validates that a string is not null, not empty, and does not exceed the maximum length.
     * 
     * @param value the string to validate
     * @param fieldName the name of the field being validated
     * @param maxLength the maximum allowed length
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateString(String value, String fieldName, int maxLength) {
        validateNotNull(value, fieldName);
        validateNotEmpty(value, fieldName);
        validateLength(value, fieldName, maxLength);
    }
    
    /**
     * Validates that a BigDecimal value is not null.
     * 
     * @param value the BigDecimal to validate
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the value is null
     */
    public static void validateNotNull(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
    
    /**
     * Validates that a BigDecimal value is greater than zero.
     * 
     * @param value the BigDecimal to validate
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the value is not greater than zero
     */
    public static void validateGreaterThanZero(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
    }
    
    /**
     * Validates that a BigDecimal value is positive (greater than or equal to zero).
     * 
     * @param value the BigDecimal to validate
     * @param fieldName the name of the field being validated
     * @throws IllegalArgumentException if the value is negative
     */
    public static void validatePositive(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
    
    /**
     * Validates a transaction description.
     * Must be not null, not empty, and not exceed 200 characters.
     * 
     * @param description the transaction description to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionDescription(String description) {
        validateString(description, "Transaction description", 200);
    }
    
    /**
     * Validates a transaction amount.
     * Must be not null and greater than zero.
     * 
     * @param amount the transaction amount to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionAmount(BigDecimal amount) {
        validateNotNull(amount, "Transaction amount");
        validateGreaterThanZero(amount, "Transaction amount");
    }
    
    /**
     * Validates a transaction date.
     * Must be not null.
     * 
     * @param date the transaction date to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionDate(java.time.LocalDate date) {
        validateNotNull(date, "Transaction date");
        validateEqual(date, java.time.LocalDate.class, "Transaction date");
    }
    
    /**
     * Validates a transaction category.
     * Must be not null.
     * 
     * @param category the transaction category to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionCategory(Category category) {
        validateNotNull(category, "Transaction category");
        validateEqual(category, Category.class, "Transaction category");
    }
    
    /**
     * Validates transaction notes.
     * Must not exceed 500 characters if not null.
     * 
     * @param notes the transaction notes to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionNotes(String notes) {
        validateLength(notes, "Transaction notes", 500);
    }
    
    /**
     * Validates a category name.
     * Must be not null, not empty, and not exceed 50 characters.
     * 
     * @param name the category name to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateCategoryName(String name) {
        validateString(name, "Category name", 50);
    }
    
    /**
     * Validates a category description.
     * Must be not null and not exceed 200 characters.
     * 
     * @param description the category description to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateCategoryDescription(String description) {
        validateNotNull(description, "Category description");
        validateLength(description, "Category description", 200);
    }
    
    /**
     * Validates a transaction type.
     * Must be not null.
     * 
     * @param type the transaction type to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionType(TransactionType type) {
        validateNotNull(type, "Transaction type");
        validateEqual(type, TransactionType.class, "Transaction type");
    }
    
    /**
     * Validates a budget category.
     * Must be not null and must be an expense category.
     * 
     * @param category the budget category to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateBudgetCategory(Category category) {
        validateNotNull(category, "Budget category");
        if (!category.getTransactionType().equals(TransactionType.EXPENSE)) {
            throw new IllegalArgumentException("Budgets only apply to expense categories");
        }
    }
    
    /**
     * Validates a budget limit.
     * Must be not null and greater than zero.
     * 
     * @param limit the budget limit to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateBudgetLimit(BigDecimal limit) {
        validateNotNull(limit, "Budget limit");
        validateGreaterThanZero(limit, "Budget limit");
    }
    
    /**
     * Validates a transaction type string.
     * Must be a valid transaction type (INCOME, EXPENSE, TRANSFER).
     * 
     * @param type the transaction type string to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateTransactionTypeString(String type) {
        validateNotEmpty(type, "Transaction type");
        if (!TransactionType.isValidType(type)) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }
}
package com.budget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.budget.model.Category;
import com.budget.model.TransactionType;

class ValidationServiceTest {
    
    private final Category foodCategory = new Category("Food", "Groceries", TransactionType.EXPENSE);
    private final Category salaryCategory = new Category("Salary", "Monthly income", TransactionType.INCOME);
    
    @Test
    void testValidateNotNullValid() {
        ValidationService.validateNotNull("test", "test field");
        ValidationService.validateNotNull(BigDecimal.ONE, "test field");
        ValidationService.validateNotNull(LocalDate.now(), "test field");
        ValidationService.validateNotNull(foodCategory, "test field");
    }
    
    @Test
    void testValidateNotNullNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateNotNull(null, "test field"));
    }
    
    @Test
    void testValidateNotEmptyValid() {
        ValidationService.validateNotEmpty("test", "test field");
        ValidationService.validateNotEmpty("   test   ", "test field");
    }
    
    @Test
    void testValidateNotEmptyNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateNotEmpty(null, "test field"));
    }

    @Test
    void testValidateNotEmptyEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateNotEmpty("", "test field"));
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateNotEmpty("   ", "test field"));
    }
    
    @Test
    void testValidateLengthValid() {
        ValidationService.validateLength("test", "test field", 10);
        ValidationService.validateLength("test", "test field", 4);
        ValidationService.validateLength(null, "test field", 10);
    }
    
    @Test
    void testValidateLengthTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateLength("testtesttest", "test field", 10));
    }
    
    @Test
    void testValidateStringValid() {
        ValidationService.validateString("test", "test field", 10);
        ValidationService.validateString("test", "test field", 4);
    }
    
    @Test
    void testValidateStringNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateString(null, "test field", 10));
    }
    
    @Test
    void testValidateStringEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateString("", "test field", 10));
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateString("   ", "test field", 10));
    }
    
    @Test
    void testValidateStringTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateString("testtesttest", "test field", 10));
    }
    
    @Test
    void testValidateNotNullBigDecimalValid() {
        ValidationService.validateNotNull(BigDecimal.ONE, "test field");
    }
    
    @Test
    void testValidateNotNullBigDecimalNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateNotNull(null, "test field"));
    }
    
    @Test
    void testValidateGreaterThanZeroValid() {
        ValidationService.validateGreaterThanZero(BigDecimal.ONE, "test field");
        ValidationService.validateGreaterThanZero(new BigDecimal("0.01"), "test field");
    }
    
    @Test
    void testValidateGreaterThanZeroZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateGreaterThanZero(BigDecimal.ZERO, "test field"));
    }
    
    @Test
    void testValidateGreaterThanZeroNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateGreaterThanZero(new BigDecimal("-1"), "test field"));
    }
    
    @Test
    void testValidatePositiveValid() {
        ValidationService.validatePositive(BigDecimal.ONE, "test field");
        ValidationService.validatePositive(BigDecimal.ZERO, "test field");
        ValidationService.validatePositive(new BigDecimal("0.01"), "test field");
    }
    
    @Test
    void testValidatePositiveNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validatePositive(new BigDecimal("-1"), "test field"));
    }
    
    @Test
    void testValidateTransactionDescriptionValid() {
        ValidationService.validateTransactionDescription("test");
        ValidationService.validateTransactionDescription("a".repeat(200));
    }
    
    @Test
    void testValidateTransactionDescriptionTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionDescription("a".repeat(201)));
    }
    
    @Test
    void testValidateTransactionAmountValid() {
        ValidationService.validateTransactionAmount(BigDecimal.ONE);
        ValidationService.validateTransactionAmount(new BigDecimal("0.01"));
    }
    
    @Test
    void testValidateTransactionAmountZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionAmount(BigDecimal.ZERO));
    }
    
    @Test
    void testValidateTransactionAmountNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionAmount(new BigDecimal("-1")));
    }
    
    @Test
    void testValidateTransactionDateValid() {
        ValidationService.validateTransactionDate(LocalDate.now());
    }
    
    @Test
    void testValidateTransactionDateNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionDate(null));
    }
    
    @Test
    void testValidateTransactionCategoryValid() {
        ValidationService.validateTransactionCategory(foodCategory);
    }
    
    @Test
    void testValidateTransactionCategoryNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionCategory(null));
    }
    
    @Test
    void testValidateTransactionNotesValid() {
        ValidationService.validateTransactionNotes(null);
        ValidationService.validateTransactionNotes("");
        ValidationService.validateTransactionNotes("test");
        ValidationService.validateTransactionNotes("a".repeat(500));
    }
    
    @Test
    void testValidateTransactionNotesTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionNotes("a".repeat(501)));
    }
    
    @Test
    void testValidateCategoryNameValid() {
        ValidationService.validateCategoryName("test");
        ValidationService.validateCategoryName("a".repeat(50));
    }
    
    @Test
    void testValidateCategoryNameTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateCategoryName("a".repeat(51)));
    }
    
    @Test
    void testValidateCategoryDescriptionValid() {
        ValidationService.validateCategoryDescription("test");
        ValidationService.validateCategoryDescription("a".repeat(200));
    }
    
    @Test
    void testValidateCategoryDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateCategoryDescription(null));
    }
    
    @Test
    void testValidateCategoryDescriptionTooLong() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateCategoryDescription("a".repeat(201)));
    }
    
    @Test
    void testValidateTransactionTypeValid() {
        ValidationService.validateTransactionType(TransactionType.EXPENSE);
        ValidationService.validateTransactionType(TransactionType.INCOME);
        ValidationService.validateTransactionType(TransactionType.TRANSFER);
    }
    
    @Test
    void testValidateTransactionTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionType(null));
    }
    
    @Test
    void testValidateBudgetCategoryValid() {
        ValidationService.validateBudgetCategory(foodCategory);
    }
    
    @Test
    void testValidateBudgetCategoryNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateBudgetCategory(null));
    }
    
    @Test
    void testValidateBudgetCategoryIncome() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateBudgetCategory(salaryCategory));
    }
    
    @Test
    void testValidateBudgetLimitValid() {
        ValidationService.validateBudgetLimit(BigDecimal.ONE);
        ValidationService.validateBudgetLimit(new BigDecimal("0.01"));
    }
    
    @Test
    void testValidateBudgetLimitZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateBudgetLimit(BigDecimal.ZERO));
    }
    
    @Test
    void testValidateBudgetLimitNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateBudgetLimit(new BigDecimal("-1")));
    }
    
    @Test
    void testValidateTransactionTypeStringValid() {
        ValidationService.validateTransactionTypeString("EXPENSE");
        ValidationService.validateTransactionTypeString("INCOME");
        ValidationService.validateTransactionTypeString("TRANSFER");
        ValidationService.validateTransactionTypeString("expense");
        ValidationService.validateTransactionTypeString("income");
        ValidationService.validateTransactionTypeString("transfer");
    }
    
    @Test
    void testValidateTransactionTypeStringNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionTypeString(null));
    }
    
    @Test
    void testValidateTransactionTypeStringEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionTypeString(""));
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionTypeString("   "));
    }
    
    @Test
    void testValidateTransactionTypeStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionTypeString("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> 
            ValidationService.validateTransactionTypeString("income123"));
    }
}
package com.budget.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.budget.model.Transaction;
import com.budget.model.Category;
import com.budget.model.TransactionType;
import com.budget.service.TransactionService;

class BudgetTrackerCLITest {
    
    private BudgetTrackerCLI cli;
    
    @BeforeEach
    void setUp() {
        cli = new BudgetTrackerCLI(null);
    }
    
    @Test
    void testStart() {
        // Test that start method doesn't throw exceptions
        assertDoesNotThrow(() -> cli.start());
    }
    
    @Test
    void testReadStringValidation() {
        // Test that readString properly validates input
        assertThrows(IllegalArgumentException.class, () -> {
            cli.readString("", 10);
        });
    }
    
    @Test
    void testReadBigDecimalValidation() {
        // Test that readBigDecimal properly validates input
        assertThrows(IllegalArgumentException.class, () -> {
            cli.readBigDecimal("invalid");
        });
    }
    
    @Test
    void testReadDateValidation() {
        // Test that readDate properly validates input
        assertThrows(IllegalArgumentException.class, () -> {
            cli.readDate("invalid");
        });
    }
    
    @Test
    void testReadTransactionType() {
        // Test that readTransactionType handles valid choices
        assertNotNull(cli.readTransactionType());
    }
    
    @Test
    void testReadCategory() {
        // Test that readCategory handles valid input
        assertNotNull(cli.readCategory(TransactionType.EXPENSE));
    }
}
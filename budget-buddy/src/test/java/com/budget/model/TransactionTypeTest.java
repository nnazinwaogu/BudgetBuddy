package com.budget.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TransactionTypeTest {
    
    @Test
    void testValidTransactionTypes() {
        assertTrue(TransactionType.isValidType("INCOME"));
        assertTrue(TransactionType.isValidType("EXPENSE"));
        assertTrue(TransactionType.isValidType("TRANSFER"));
    }
    
    @Test
    void testInvalidTransactionTypes() {
        assertFalse(TransactionType.isValidType(null));
        assertFalse(TransactionType.isValidType(""));
        assertTrue(TransactionType.isValidType("income"));
        assertFalse(TransactionType.isValidType("invalid"));
    }
    
    @Test
    void testFromStringValid() {
        assertEquals(TransactionType.INCOME, TransactionType.fromString("INCOME"));
        assertEquals(TransactionType.EXPENSE, TransactionType.fromString("EXPENSE"));
        assertEquals(TransactionType.TRANSFER, TransactionType.fromString("TRANSFER"));
    }
    
    @Test
    void testFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> TransactionType.fromString(null));
        assertThrows(IllegalArgumentException.class, () -> TransactionType.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> TransactionType.fromString("invalid"));
    }
    
    @Test
    void testFromStringCaseInsensitive() {
        assertEquals(TransactionType.INCOME, TransactionType.fromString("income"));
        assertEquals(TransactionType.EXPENSE, TransactionType.fromString("expense"));
        assertEquals(TransactionType.TRANSFER, TransactionType.fromString("transfer"));
    }
}
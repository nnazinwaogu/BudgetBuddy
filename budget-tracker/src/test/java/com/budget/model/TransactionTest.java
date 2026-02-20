package com.budget.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TransactionTest {
    
    private final Category foodCategory = new Category("Food", "Groceries", TransactionType.EXPENSE);
    //private final Category salaryCategory = new Category("Salary", "Monthly income", TransactionType.INCOME);
    
    @Test
    void testValidTransactionCreation() {
        Transaction transaction = new Transaction( 
            "Grocery shopping at Walmart", 
            new BigDecimal("45.99"), 
            LocalDate.of(2024, 1, 15), 
            foodCategory,
            "Weekly groceries"
        );
        
        assertNotNull(transaction.getId());
        assertEquals(String.class, transaction.getId().getClass());
        assertNotNull(transaction.getLoggedTime());
        assertEquals("Grocery shopping at Walmart", transaction.getDescription());
        assertEquals(new BigDecimal("45.99"), transaction.getAmount());
        assertEquals(LocalDate.of(2024, 1, 15), transaction.getDate());
        assertEquals(foodCategory, transaction.getCategory());
        assertEquals("Weekly groceries", transaction.getNotes());
    }
    
    @Test
    void testTransactionValidation() {
    
        assertThrows(IllegalArgumentException.class, () -> new Transaction(null, new BigDecimal("10"), LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("", new BigDecimal("10"), LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("a".repeat(201), new BigDecimal("10"), LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", null, LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", new BigDecimal("-10"), LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", BigDecimal.ZERO, LocalDate.now(), foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", new BigDecimal("10"), null, foodCategory, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", new BigDecimal("10"), LocalDate.now(), null, "notes"));
        assertThrows(IllegalArgumentException.class, () -> new Transaction("desc", new BigDecimal("10"), LocalDate.now(), foodCategory, "a".repeat(501)));
    }
    
    @Test
    void testTransactionEquality() {
        Transaction transaction1 = new Transaction("desc", new BigDecimal("10"), LocalDate.now(), foodCategory, "notes");
        Transaction transaction2 = transaction1;
        Transaction transaction3 = new Transaction("different desc", new BigDecimal("10"), LocalDate.now(), foodCategory, "notes");
        
        assertEquals(transaction1, transaction2);
        assertNotEquals(transaction1, transaction3);
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, new Object());
    }
    
    @Test
    void testTransactionHashCode() {
        Transaction transaction1 = new Transaction("desc", new BigDecimal("10"), LocalDate.now(), foodCategory, "notes");
        Transaction transaction2 = transaction1;
        
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }
    
    @Test
    void testTransactionToString() {
        Transaction transaction = new Transaction("Grocery shopping", new BigDecimal("45.99"), LocalDate.of(2024, 1, 15), foodCategory, "Weekly groceries");
        String expected = "Transaction{id='" + transaction.getId() + "', description='Grocery shopping', amount=45.99, date=2024-01-15, loggedTime=" + transaction.getLoggedTime() + ", category=Category{name='Food', description='Groceries', transactionType=EXPENSE}, notes='Weekly groceries'}";
        assertEquals(expected, transaction.toString());
    }
    
    @Test
    void testTransactionNotesOptional() {
        Transaction transaction = new Transaction("desc", new BigDecimal("10"), LocalDate.now(), foodCategory, null);
        assertNull(transaction.getNotes());
        
        transaction = new Transaction("desc", new BigDecimal("10"), LocalDate.now(), foodCategory, "   ");
        assertEquals("", transaction.getNotes());
    }
}
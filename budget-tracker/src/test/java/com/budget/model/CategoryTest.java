package com.budget.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CategoryTest {
    
    @Test
    void testValidCategoryCreation() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        
        assertEquals("Food", category.getName());
        assertEquals("Groceries and dining out", category.getDescription());
        assertEquals(TransactionType.EXPENSE, category.getTransactionType());
    }
    
    @Test
    void testCategoryValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Category(null, "desc", TransactionType.EXPENSE));
        assertThrows(IllegalArgumentException.class, () -> new Category("", "desc", TransactionType.EXPENSE));
        assertThrows(IllegalArgumentException.class, () -> new Category("a".repeat(51), "desc", TransactionType.EXPENSE));
        assertThrows(IllegalArgumentException.class, () -> new Category("Food", null, TransactionType.EXPENSE));
        assertThrows(IllegalArgumentException.class, () -> new Category("Food", "a".repeat(201), TransactionType.EXPENSE));
        assertThrows(IllegalArgumentException.class, () -> new Category("Food", "desc", null));
    }
    
    @Test
    void testCategoryEquality() {
        Category category1 = new Category("Food", "Groceries", TransactionType.EXPENSE);
        Category category2 = new Category("Food", "Dining out", TransactionType.EXPENSE);
        Category category3 = new Category("Income", "Salary", TransactionType.INCOME);
        
        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertNotEquals(category1, null);
        assertNotEquals(category1, new Object());
    }
    
    @Test
    void testCategoryHashCode() {
        Category category1 = new Category("Food", "Groceries", TransactionType.EXPENSE);
        Category category2 = new Category("Food", "Dining out", TransactionType.EXPENSE);
        
        assertEquals(category1.hashCode(), category2.hashCode());
    }
    
    @Test
    void testCategoryToString() {
        Category category = new Category("Food", "Groceries", TransactionType.EXPENSE);
        String expected = "Category{name='Food', description='Groceries', transactionType=EXPENSE}";
        assertEquals(expected, category.toString());
    }
}
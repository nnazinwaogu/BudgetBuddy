package com.budget.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;


//import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class BudgetTest {

    @Test
    void testValidBudgetCreation() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        Budget budget = new Budget(category, BigDecimal.valueOf(500));
        
        assertEquals("Food", category.getName());
        assertEquals("Groceries and dining out", category.getDescription());
        assertEquals(TransactionType.EXPENSE, category.getTransactionType());
        assertEquals(category, budget.getCategory());
        assertEquals(BigDecimal.valueOf(500), budget.getMonthlyLimit());
    }

    @Test
    void testBudgetValidation() {
        Category invalidCategory = new Category("Cheques", "Cheques Received", TransactionType.INCOME); //using this cat should throw IllegalArgumentException       
        Category validCategory = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE); //using this cat should not throw IllegalArgumentException

        assertThrows(IllegalArgumentException.class, () -> new Budget(invalidCategory, BigDecimal.valueOf(500)));
        assertThrows(IllegalArgumentException.class, () -> new Budget(validCategory, BigDecimal.valueOf(-500)));
        assertThrows(IllegalArgumentException.class, () -> new Budget(validCategory, BigDecimal.valueOf(-0)));

    }

    @Test
    void testSetMonthlyLimit() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE); 
        Budget budget = new Budget(category, BigDecimal.valueOf(500));

        assertThrows(IllegalArgumentException.class, () -> budget.setMonthlyLimit(BigDecimal.valueOf(-500)));
        assertThrows(IllegalArgumentException.class, () -> budget.setMonthlyLimit(BigDecimal.valueOf(-0)));
        budget.setMonthlyLimit(BigDecimal.valueOf(1000));
        assertEquals(BigDecimal.valueOf(1000), budget.getMonthlyLimit());
    }

    @Test
    void testGetMonthlyLimit() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE); 
        Budget budget = new Budget(category, BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), budget.getMonthlyLimit());
    }

    @Test
    void testGetCategory() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE); 
        Budget budget = new Budget(category, BigDecimal.valueOf(500));

        assertEquals(category, budget.getCategory());
    }

     @Test
    void testBudgetEquality() {
        Category category1 = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        Category category2 = new Category("Bill", "Hydro Bill", TransactionType.EXPENSE);
        Budget budget1 = new Budget(category1, BigDecimal.valueOf(500));
        Budget budget2 = new Budget(category1, BigDecimal.valueOf(500));
        Budget budget3 = new Budget(category2, BigDecimal.valueOf(500));

        assertEquals(budget1, budget2);
        assertEquals(budget1.hashCode(), budget2.hashCode());
        assertNotEquals(budget1, budget3);
        assertNotEquals(budget1, null);         
        assertNotEquals(budget1, new Object());
    }
    
    @Test
    void testBudgetHashCode() {
        Category category1 = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        Category category2 = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        Budget budget1 = new Budget(category1, BigDecimal.valueOf(500));
        Budget budget2 = new Budget(category2, BigDecimal.valueOf(500));
        
        assertEquals(budget1.hashCode(), budget2.hashCode());
    }
    
    @Test
    void testBudgetToString() {
        Category category = new Category("Food", "Groceries and dining out", TransactionType.EXPENSE);
        Budget budget = new Budget(category, BigDecimal.valueOf(500));
        String expected = "Budget{category=" + category + ", monthlyLimit=500}";
        assertEquals(expected, budget.toString());
    }

}

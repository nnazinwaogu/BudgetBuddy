package com.budget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;
import com.budget.repository.BudgetRepository;
import com.budget.repository.InMemoryBudgetRepository;
import com.budget.repository.TransactionRepository;
import com.budget.repository.InMemoryTransactionRepository;

class BudgetServiceTest {

    private BudgetService budgetService;
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;

    private Category foodCategory;
    private Category transportCategory;
    private Budget foodBudget;
    private Budget transportBudget;

    @BeforeEach
    void setUp() {
        budgetRepository = new InMemoryBudgetRepository();
        transactionRepository = new InMemoryTransactionRepository();
        budgetService = new BudgetService(budgetRepository, transactionRepository);

        foodCategory = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        transportCategory = new Category("Transport", "Transportation costs", TransactionType.EXPENSE);
        foodBudget = new Budget(foodCategory, new BigDecimal("500.00"));
        transportBudget = new Budget(transportCategory, new BigDecimal("300.00"));
    }

    @Test
    void testSaveBudget() {
        Budget result = budgetService.saveBudget(foodBudget);
        assertEquals(foodBudget, result);

        Optional<Budget> found = budgetService.getBudgetByCategory(foodCategory);
        assertTrue(found.isPresent());
        assertEquals(foodBudget, found.get());
    }

    @Test
    void testUpdateBudget() {
        budgetService.saveBudget(foodBudget);
        Budget updated = budgetService.updateBudget(foodCategory, new BigDecimal("600.00"));
        assertEquals(new BigDecimal("600.00"), updated.getMonthlyLimit());

        Optional<Budget> found = budgetService.getBudgetByCategory(foodCategory);
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("600.00"), found.get().getMonthlyLimit());
    }

    @Test
    void testUpdateBudgetNotFound() {
        assertThrows(IllegalArgumentException.class,
            () -> budgetService.updateBudget(foodCategory, new BigDecimal("600.00")));
    }

    @Test
    void testDeleteBudget() {
        budgetService.saveBudget(foodBudget);
        assertTrue(budgetService.deleteBudget(foodCategory));
        assertFalse(budgetService.getBudgetByCategory(foodCategory).isPresent());
    }

    @Test
    void testDeleteBudgetNotFound() {
        assertFalse(budgetService.deleteBudget(foodCategory));
    }

    @Test
    void testGetBudgetByCategory() {
        budgetService.saveBudget(foodBudget);
        Optional<Budget> found = budgetService.getBudgetByCategory(foodCategory);
        assertTrue(found.isPresent());
        assertEquals(foodBudget, found.get());
    }

    @Test
    void testGetBudgetByCategoryNotFound() {
        Optional<Budget> found = budgetService.getBudgetByCategory(foodCategory);
        assertFalse(found.isPresent());
    }

    @Test
    void testGetAllBudgets() {
        budgetService.saveBudget(foodBudget);
        budgetService.saveBudget(transportBudget);

        List<Budget> all = budgetService.getAllBudgets();
        assertEquals(2, all.size());
        assertTrue(all.contains(foodBudget));
        assertTrue(all.contains(transportBudget));
    }

    @Test
    void testCalculateSpending() {
        budgetService.saveBudget(foodBudget);

        transactionRepository.save(new Transaction("Lunch", new BigDecimal("15.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null));
        transactionRepository.save(new Transaction("Dinner", new BigDecimal("25.00"),
            LocalDate.of(2024, 1, 20), foodCategory, null));

        // Different category — should not count
        transactionRepository.save(new Transaction("Bus", new BigDecimal("5.00"),
            LocalDate.of(2024, 1, 16), transportCategory, null));

        // Different month — should not count
        transactionRepository.save(new Transaction("Brunch", new BigDecimal("10.00"),
            LocalDate.of(2024, 2, 1), foodCategory, null));

        BigDecimal spending = budgetService.calculateSpending(foodCategory, YearMonth.of(2024, 1));
        assertEquals(new BigDecimal("40.00"), spending);
    }

    @Test
    void testCalculateSpendingNoTransactions() {
        budgetService.saveBudget(foodBudget);
        BigDecimal spending = budgetService.calculateSpending(foodCategory, YearMonth.of(2024, 1));
        assertEquals(BigDecimal.ZERO, spending);
    }

    @Test
    void testGetBudgetStatus() {
        budgetService.saveBudget(foodBudget);
        transactionRepository.save(new Transaction("Lunch", new BigDecimal("200.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null));

        BudgetService.BudgetStatus status = budgetService.getBudgetStatus(foodCategory, YearMonth.of(2024, 1));
        assertEquals(foodCategory, status.category());
        assertEquals(new BigDecimal("500.00"), status.limit());
        assertEquals(new BigDecimal("200.00"), status.spending());
        assertEquals(0, new BigDecimal("40.00").compareTo(status.percentageUsed()));
    }

    @Test
    void testGetBudgetStatusNoBudgetSet() {
        assertThrows(IllegalArgumentException.class,
            () -> budgetService.getBudgetStatus(foodCategory, YearMonth.of(2024, 1)));
    }

    @Test
    void testIsOverBudget() {
        budgetService.saveBudget(foodBudget);

        // 200 / 500 = 40 % — not over budget
        transactionRepository.save(new Transaction("Lunch", new BigDecimal("200.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null));
        assertFalse(budgetService.isOverBudget(foodCategory, YearMonth.of(2024, 1)));

        // Add more to exceed 80 %: 450 / 500 = 90 %
        transactionRepository.save(new Transaction("Dinner", new BigDecimal("250.00"),
            LocalDate.of(2024, 1, 20), foodCategory, null));
        assertTrue(budgetService.isOverBudget(foodCategory, YearMonth.of(2024, 1)));
    }

    @Test
    void testIsOverBudgetExactlyAtThreshold() {
        budgetService.saveBudget(foodBudget);

        // 400 / 500 = 80 % — exactly at threshold, NOT over budget
        transactionRepository.save(new Transaction("Shopping", new BigDecimal("400.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null));
        assertFalse(budgetService.isOverBudget(foodCategory, YearMonth.of(2024, 1)));
    }

    @Test
    void testNullInputs() {
        assertThrows(NullPointerException.class, () -> budgetService.saveBudget(null));
        assertThrows(NullPointerException.class, () -> budgetService.updateBudget(null, BigDecimal.TEN));
        assertThrows(NullPointerException.class, () -> budgetService.updateBudget(foodCategory, null));
        assertThrows(NullPointerException.class, () -> budgetService.deleteBudget(null));
        assertThrows(NullPointerException.class, () -> budgetService.getBudgetByCategory(null));
        assertThrows(NullPointerException.class, () -> budgetService.calculateSpending(null, YearMonth.now()));
        assertThrows(NullPointerException.class, () -> budgetService.calculateSpending(foodCategory, null));
        assertThrows(NullPointerException.class, () -> budgetService.getBudgetStatus(null, YearMonth.now()));
        assertThrows(NullPointerException.class, () -> budgetService.getBudgetStatus(foodCategory, null));
        assertThrows(NullPointerException.class, () -> budgetService.isOverBudget(null, YearMonth.now()));
        assertThrows(NullPointerException.class, () -> budgetService.isOverBudget(foodCategory, null));
    }
}
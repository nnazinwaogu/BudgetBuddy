package com.budget.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.repository.BudgetRepository;
import com.budget.repository.TransactionRepository;

public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(BudgetRepository budgetRepository, TransactionRepository transactionRepository) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository, "BudgetRepository cannot be null");
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "TransactionRepository cannot be null");
    }

    public Budget saveBudget(Budget budget) {
        Objects.requireNonNull(budget, "Budget cannot be null");
        budgetRepository.save(budget);
        return budget;
    }

    public Budget updateBudget(Category category, BigDecimal newLimit) {
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(newLimit, "New limit cannot be null");
        Optional<Budget> existing = budgetRepository.findByCategory(category);
        if (existing.isPresent()) {
            Budget budget = existing.get();
            budget.setMonthlyLimit(newLimit);
            budgetRepository.save(budget);
            return budget;
        }
        throw new IllegalArgumentException("Budget not found for category: " + category.getName());
    }

    public boolean deleteBudget(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        if (budgetRepository.exists(category)) {
            budgetRepository.delete(category);
            return true;
        }
        return false;
    }

    public Optional<Budget> getBudgetByCategory(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        return budgetRepository.findByCategory(category);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public BigDecimal calculateSpending(Category category, YearMonth yearMonth) {
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(yearMonth, "YearMonth cannot be null");
        return transactionRepository.findAll().stream()
            .filter(t -> t.getCategory().equals(category))
            .filter(t -> YearMonth.from(t.getDate()).equals(yearMonth))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BudgetStatus getBudgetStatus(Category category, YearMonth yearMonth) {
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(yearMonth, "YearMonth cannot be null");
        Optional<Budget> budgetOpt = budgetRepository.findByCategory(category);
        if (budgetOpt.isEmpty()) {
            throw new IllegalArgumentException("No budget set for category: " + category.getName());
        }
        Budget budget = budgetOpt.get();
        BigDecimal spending = calculateSpending(category, yearMonth);
        BigDecimal limit = budget.getMonthlyLimit();
        BigDecimal percentage = spending.divide(limit, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
        return new BudgetStatus(category, limit, spending, percentage);
    }

    public boolean isOverBudget(Category category, YearMonth yearMonth) {
        BudgetStatus status = getBudgetStatus(category, yearMonth);
        return status.percentageUsed().compareTo(new BigDecimal("80.00")) > 0;
    }

    public record BudgetStatus(Category category, BigDecimal limit, BigDecimal spending, BigDecimal percentageUsed) {}
}
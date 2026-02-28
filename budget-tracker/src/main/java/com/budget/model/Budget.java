package com.budget.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Budget {
    private final Category category;      // Immutable once set
    private BigDecimal monthlyLimit;      // Can be updated
    
    public Budget(Category category, BigDecimal monthlyLimit) {
        // Validate: category must be expense category, limit > 0
        if (!category.getTransactionType().equals(TransactionType.EXPENSE)) {
            throw new IllegalArgumentException("Budgets only apply to expense categories");
        }
        if (monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget limit must be positive");
        }
        this.category = category;
        this.monthlyLimit = monthlyLimit;
    }
    
    
    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        if (monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget limit must be positive");
        }
        this.monthlyLimit = monthlyLimit;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public Category getCategory() {
        return category;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Budget budget = (Budget) obj;
        return Objects.equals(category, budget.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
    
    @Override
    public String toString() {
        return "Budget{" +
                "category=" + category +
                ", monthlyLimit=" + monthlyLimit +
                '}';
    }
}
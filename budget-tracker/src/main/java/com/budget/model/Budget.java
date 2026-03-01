package com.budget.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.budget.service.ValidationService;

public class Budget {
    private final Category category;      // Immutable once set
    private BigDecimal monthlyLimit;      // Can be updated
    
    public Budget(Category category, BigDecimal monthlyLimit) {
        // Validate using ValidationService
        ValidationService.validateBudgetCategory(category);
        ValidationService.validateBudgetLimit(monthlyLimit);
        
        this.category = category;
        this.monthlyLimit = monthlyLimit;
    }
    
    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        ValidationService.validateBudgetLimit(monthlyLimit);
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
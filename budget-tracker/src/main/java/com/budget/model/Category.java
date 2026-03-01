package com.budget.model;

import java.util.Objects;

import com.budget.service.ValidationService;

public final class Category {
    private final String name;
    private final String description;
    private final TransactionType transactionType;
    
    public Category(String name, String description, TransactionType transactionType) {
        // Validate using ValidationService
        ValidationService.validateCategoryName(name);
        ValidationService.validateCategoryDescription(description);
        ValidationService.validateTransactionType(transactionType);
        
        this.name = name.trim();
        this.description = description.trim();
        this.transactionType = transactionType;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name) && 
               transactionType == category.transactionType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, transactionType);
    }
    
    @Override
    public String toString() {
        return "Category{name='" + name + "', description='" + description + "', transactionType=" + transactionType + "}";
    }
}
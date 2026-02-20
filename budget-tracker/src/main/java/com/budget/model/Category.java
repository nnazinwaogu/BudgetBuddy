package com.budget.model;

import java.util.Objects;

public final class Category {
    private final String name;
    private final String description;
    private final TransactionType transactionType;
    
    public Category(String name, String description, TransactionType transactionType) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Category name cannot exceed 50 characters");
        }
        if (description == null) {
            throw new IllegalArgumentException("Category description cannot be null");
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException("Category description cannot exceed 200 characters");
        }
        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
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
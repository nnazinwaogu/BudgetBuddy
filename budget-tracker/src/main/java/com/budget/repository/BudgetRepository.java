package com.budget.repository;

import java.util.List;
import java.util.Optional;

import com.budget.model.Budget;
import com.budget.model.Category;

public interface BudgetRepository {
    
    void save(Budget budget);
    
    List<Budget> findAll();
    
    Optional<Budget> findByCategory(Category category);
    
    void delete(Category category);
    
    boolean exists(Category category);
}
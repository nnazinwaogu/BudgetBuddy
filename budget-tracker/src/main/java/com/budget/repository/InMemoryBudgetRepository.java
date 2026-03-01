package com.budget.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Objects;

import com.budget.model.Budget;
import com.budget.model.Category;

public class InMemoryBudgetRepository implements BudgetRepository {
    
    private final Map<Category, Budget> budgets = new HashMap<>();
    private final List<Budget> budgetList = new ArrayList<>();
    
    @Override
    public synchronized void save(Budget budget) {
        Objects.requireNonNull(budget, "Budget cannot be null");
        budgets.put(budget.getCategory(), budget);
        budgetList.add(budget);
    }
    
    @Override
    public synchronized List<Budget> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(budgetList));
    }
    
    @Override
    public synchronized Optional<Budget> findByCategory(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        return Optional.ofNullable(budgets.get(category));
    }
    
    @Override
    public synchronized void delete(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        Budget removed = budgets.remove(category);
        if (removed != null) {
            budgetList.remove(removed);
        }
    }
    
    @Override
    public synchronized boolean exists(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        return budgets.containsKey(category);
    }
}
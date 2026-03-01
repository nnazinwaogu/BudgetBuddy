package com.budget.repository;

import java.util.List;
import java.util.Optional;

import com.budget.model.Category;

public interface CategoryRepository {
    
    void save(Category category);
    
    List<Category> findAll();
    
    Optional<Category> findById(String name);
    
    void delete(String name);
    
    boolean exists(String name);
}
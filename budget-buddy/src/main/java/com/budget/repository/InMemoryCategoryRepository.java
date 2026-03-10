package com.budget.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Objects;

import com.budget.model.Category;

public class InMemoryCategoryRepository implements CategoryRepository {
    
    private final Map<String, Category> categories = new HashMap<>();
    private final List<Category> categoryList = new ArrayList<>();
    
    @Override
    public synchronized void save(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        categories.put(category.getName(), category);
        categoryList.add(category);
    }
    
    @Override
    public synchronized List<Category> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(categoryList));
    }
    
    @Override
    public synchronized Optional<Category> findById(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        return Optional.ofNullable(categories.get(name));
    }
    
    @Override
    public synchronized void delete(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        Category removed = categories.remove(name);
        if (removed != null) {
            categoryList.remove(removed);
        }
    }
    
    @Override
    public synchronized boolean exists(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        return categories.containsKey(name);
    }
}
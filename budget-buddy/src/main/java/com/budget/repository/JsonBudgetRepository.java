package com.budget.repository;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.util.FileUtil;
import com.budget.util.JacksonJsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JSON file-based implementation of BudgetRepository.
 * Persists budgets to a JSON file and loads them on initialization.
 * Thread-safe with synchronized methods.
 */
public class JsonBudgetRepository implements BudgetRepository {
    
    private final String filePath;
    private final List<Budget> budgets;
    
    /**
     * Constructs a JsonBudgetRepository with the specified file path.
     * Loads existing budgets from the file, or creates empty storage if file doesn't exist.
     *
     * @param filePath the path to the JSON file for storing budgets
     * @throws IOException if an I/O error occurs during loading
     */
    public JsonBudgetRepository(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        this.filePath = filePath;
        this.budgets = new ArrayList<>();
        
        // Load existing budgets if file exists
        if (FileUtil.fileExists(filePath)) {
            loadAll();
        }
    }
    
    /**
     * Loads all budgets from the JSON file into memory.
     * Clears current in-memory list and populates from file.
     *
     * @throws IOException if an I/O error occurs during reading
     */
    private synchronized void loadAll() throws IOException {
        List<Budget> loaded = JacksonJsonUtil.readListFromFile(filePath, Budget.class);
        budgets.clear();
        budgets.addAll(loaded);
    }
    
    /**
     * Saves all budgets to the JSON file.
     * Serializes the entire budget list to JSON.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    private synchronized void saveAll() throws IOException {
        JacksonJsonUtil.writeListToFile(filePath, budgets);
    }
    
    @Override
    public synchronized void save(Budget budget) {
        if (budget == null) {
            throw new NullPointerException("Budget cannot be null");
        }
        
        // Remove existing budget with same category if it exists (update)
        budgets.removeIf(b -> b.getCategory().equals(budget.getCategory()));
        
        // Add new budget
        budgets.add(budget);
        
        try {
            saveAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save budget to file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public synchronized List<Budget> findAll() {
        return new ArrayList<>(budgets); // Return copy for immutability
    }
    
    @Override
    public synchronized Optional<Budget> findByCategory(Category category) {
        if (category == null) {
            throw new NullPointerException("Category cannot be null");
        }
        
        return budgets.stream()
                .filter(b -> b.getCategory().equals(category))
                .findFirst();
    }
    
    @Override
    public synchronized void delete(Category category) {
        if (category == null) {
            throw new NullPointerException("Category cannot be null");
        }
        
        boolean removed = budgets.removeIf(b -> b.getCategory().equals(category));
        if (removed) {
            try {
                saveAll();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save after deletion to file: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public synchronized boolean exists(Category category) {
        if (category == null) {
            throw new NullPointerException("Category cannot be null");
        }
        
        return budgets.stream().anyMatch(b -> b.getCategory().equals(category));
    }
    
    /**
     * Gets the total number of budgets in the repository.
     *
     * @return the count of budgets
     */
    public synchronized int count() {
        return budgets.size();
    }
}

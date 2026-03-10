package com.budget.repository;

import com.budget.model.Category;
import com.budget.util.FileUtil;
import com.budget.util.JacksonJsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JSON file-based implementation of CategoryRepository.
 * Persists categories to a JSON file and loads them on initialization.
 * Thread-safe with synchronized methods.
 */
public class JsonCategoryRepository implements CategoryRepository {
    
    private final String filePath;
    private final List<Category> categories;
    
    /**
     * Constructs a JsonCategoryRepository with the specified file path.
     * Loads existing categories from the file, or creates empty storage if file doesn't exist.
     *
     * @param filePath the path to the JSON file for storing categories
     * @throws IOException if an I/O error occurs during loading
     */
    public JsonCategoryRepository(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        this.filePath = filePath;
        this.categories = new ArrayList<>();
        
        // Load existing categories if file exists
        if (FileUtil.fileExists(filePath)) {
            loadAll();
        }
    }
    
    /**
     * Loads all categories from the JSON file into memory.
     * Clears current in-memory list and populates from file.
     *
     * @throws IOException if an I/O error occurs during reading
     */
    private synchronized void loadAll() throws IOException {
        List<Category> loaded = JacksonJsonUtil.readListFromFile(filePath, Category.class);
        categories.clear();
        categories.addAll(loaded);
    }
    
    /**
     * Saves all categories to the JSON file.
     * Serializes the entire category list to JSON.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    private synchronized void saveAll() throws IOException {
        JacksonJsonUtil.writeListToFile(filePath, categories);
    }
    
    @Override
    public synchronized void save(Category category) {
        if (category == null) {
            throw new NullPointerException("Category cannot be null");
        }
        
        // Remove existing category with same name if it exists (update)
        categories.removeIf(c -> c.getName().equals(category.getName()));
        
        // Add new category
        categories.add(category);
        
        try {
            saveAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save category to file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public synchronized List<Category> findAll() {
        return new ArrayList<>(categories); // Return copy for immutability
    }
    
    @Override
    public synchronized Optional<Category> findById(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        
        return categories.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst();
    }
    
    @Override
    public synchronized void delete(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        
        boolean removed = categories.removeIf(c -> c.getName().equals(name));
        if (removed) {
            try {
                saveAll();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save after deletion to file: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public synchronized boolean exists(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        
        return categories.stream().anyMatch(c -> c.getName().equals(name));
    }
    
    /**
     * Gets the total number of categories in the repository.
     *
     * @return the count of categories
     */
    public synchronized int count() {
        return categories.size();
    }
}

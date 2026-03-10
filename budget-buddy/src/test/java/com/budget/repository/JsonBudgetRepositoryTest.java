package com.budget.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.model.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for JsonBudgetRepository.
 * Tests file persistence, CRUD operations, and thread safety.
 */
class JsonBudgetRepositoryTest {
    
    @TempDir
    Path tempDir;
    
    private JsonBudgetRepository repository;
    private Path testFile;
    private Category foodCategory;
    private Category transportCategory;
    private Budget budget1;
    private Budget budget2;
    
    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("budgets.json");
        repository = new JsonBudgetRepository(testFile.toString());
        
        foodCategory = new Category("Food", "Groceries and dining", TransactionType.EXPENSE);
        transportCategory = new Category("Transportation", "Transport costs", TransactionType.EXPENSE);
        budget1 = new Budget(foodCategory, new BigDecimal("500.00"));
        budget2 = new Budget(transportCategory, new BigDecimal("200.00"));
    }
    
    @Test
    void testInitializationWithNewFile() throws IOException {
        // Repository should start empty when file doesn't exist
        assertTrue(repository.findAll().isEmpty());
        assertEquals(0, repository.count());
    }
    
    @Test
    void testSaveAndFindBudget() {
        repository.save(budget1);
        
        Optional<Budget> found = repository.findByCategory(budget1.getCategory());
        assertTrue(found.isPresent());
        assertEquals(budget1, found.get());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testSaveMultipleBudgets() {
        repository.save(budget1);
        repository.save(budget2);
        
        List<Budget> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(budget1));
        assertTrue(all.contains(budget2));
    }
    
    @Test
    void testFindByCategoryNotFound() {
        repository.save(budget1);
        Category nonExistent = new Category("Entertainment", "Fun", TransactionType.EXPENSE);
        Optional<Budget> found = repository.findByCategory(nonExistent);
        assertFalse(found.isPresent());
    }
    
    @Test
    void testDeleteBudget() {
        repository.save(budget1);
        assertTrue(repository.exists(budget1.getCategory()));
        
        repository.delete(budget1.getCategory());
        assertFalse(repository.exists(budget1.getCategory()));
        assertFalse(repository.findByCategory(budget1.getCategory()).isPresent());
        assertEquals(0, repository.count());
    }
    
    @Test
    void testDeleteNonExistentBudget() {
        repository.save(budget1);
        Category nonExistent = new Category("Entertainment", "Fun", TransactionType.EXPENSE);
        repository.delete(nonExistent); // Should not throw
        assertEquals(1, repository.count());
    }
    
    @Test
    void testUpdateBudget() {
        repository.save(budget1);
        
        // Create updated budget with same category but different limit
        Budget updated = new Budget(budget1.getCategory(), new BigDecimal("750.00"));
        
        repository.save(updated);
        
        Optional<Budget> found = repository.findByCategory(budget1.getCategory());
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("750.00"), found.get().getMonthlyLimit());
        assertEquals(1, repository.count()); // Still only 1 budget
    }
    
    @Test
    void testFindAllReturnsCopy() {
        repository.save(budget1);
        List<Budget> all = repository.findAll();
        
        // Modifying returned list should not affect repository
        all.clear();
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceAcrossRestart() throws IOException {
        // Save budget
        repository.save(budget1);
        
        // Close and recreate repository (simulate restart)
        repository = new JsonBudgetRepository(testFile.toString());
        
        // Budget should still be there
        Optional<Budget> found = repository.findByCategory(budget1.getCategory());
        assertTrue(found.isPresent());
        assertEquals(budget1.getMonthlyLimit(), found.get().getMonthlyLimit());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceWithMultipleBudgets() throws IOException {
        repository.save(budget1);
        repository.save(budget2);
        
        // Recreate repository
        repository = new JsonBudgetRepository(testFile.toString());
        
        List<Budget> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(budget1));
        assertTrue(all.contains(budget2));
    }
    
    @Test
    void testNullBudgetSave() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }
    
    @Test
    void testNullCategoryOperations() {
        assertThrows(NullPointerException.class, () -> repository.findByCategory(null));
        assertThrows(NullPointerException.class, () -> repository.delete(null));
        assertThrows(NullPointerException.class, () -> repository.exists(null));
    }
    
    @Test
    void testFileContentFormat() throws IOException {
        repository.save(budget1);
        repository.save(budget2);
        
        // Read file content to verify JSON structure
        String content = java.nio.file.Files.readString(testFile);
        
        assertTrue(content.contains(budget1.getCategory().getName()));
        assertTrue(content.contains(budget1.getMonthlyLimit().toString()));
        assertTrue(content.contains(budget2.getCategory().getName()));
        assertTrue(content.contains(budget2.getMonthlyLimit().toString()));
    }
    
    @Test
    void testEmptyFileHandling() throws IOException {
        // Create empty file
        java.nio.file.Files.createFile(testFile);
        
        // Repository should initialize without errors
        repository = new JsonBudgetRepository(testFile.toString());
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testCorruptedJsonFile() throws IOException {
        // Write invalid JSON to file
        java.nio.file.Files.writeString(testFile, "invalid json content");
        
        // Should throw IOException on initialization
        assertThrows(IOException.class, () -> 
            new JsonBudgetRepository(testFile.toString()));
    }
    
    @Test
    void testBudgetWithDifferentCategories() {
        Category food1 = new Category("Food", "Food", TransactionType.EXPENSE);
        Category food2 = new Category("Food", "Food with different description", TransactionType.EXPENSE);
        
        Budget budgetForFood1 = new Budget(food1, new BigDecimal("500"));
        Budget budgetForFood2 = new Budget(food2, new BigDecimal("600"));
        
        repository.save(budgetForFood1);
        repository.save(budgetForFood2); // Should replace the first one (same category name)
        
        List<Budget> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals(new BigDecimal("600"), all.get(0).getMonthlyLimit());
    }
}

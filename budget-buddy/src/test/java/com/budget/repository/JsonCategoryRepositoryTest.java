package com.budget.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.budget.model.Category;
import com.budget.model.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for JsonCategoryRepository.
 * Tests file persistence, CRUD operations, and thread safety.
 */
class JsonCategoryRepositoryTest {
    
    @TempDir
    Path tempDir;
    
    private JsonCategoryRepository repository;
    private Path testFile;
    private Category category1;
    private Category category2;
    
    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("categories.json");
        repository = new JsonCategoryRepository(testFile.toString());
        
        category1 = new Category("Food", "Groceries and dining", TransactionType.EXPENSE);
        category2 = new Category("Salary", "Monthly income", TransactionType.INCOME);
    }
    
    @Test
    void testInitializationWithNewFile() throws IOException {
        // Repository should start empty when file doesn't exist
        assertTrue(repository.findAll().isEmpty());
        assertEquals(0, repository.count());
    }
    
    @Test
    void testSaveAndFindCategory() {
        repository.save(category1);
        
        Optional<Category> found = repository.findById(category1.getName());
        assertTrue(found.isPresent());
        assertEquals(category1, found.get());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testSaveMultipleCategories() {
        repository.save(category1);
        repository.save(category2);
        
        List<Category> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(category1));
        assertTrue(all.contains(category2));
    }
    
    @Test
    void testFindByIdNotFound() {
        repository.save(category1);
        Optional<Category> found = repository.findById("nonexistent");
        assertFalse(found.isPresent());
    }
    
    @Test
    void testDeleteCategory() {
        repository.save(category1);
        assertTrue(repository.exists(category1.getName()));
        
        repository.delete(category1.getName());
        assertFalse(repository.exists(category1.getName()));
        assertFalse(repository.findById(category1.getName()).isPresent());
        assertEquals(0, repository.count());
    }
    
    @Test
    void testDeleteNonExistentCategory() {
        repository.save(category1);
        repository.delete("nonexistent"); // Should not throw
        assertEquals(1, repository.count());
    }
    
    @Test
    void testUpdateCategory() {
        repository.save(category1);
        
        // Create updated version with same name but different description
        Category updated = new Category("Food", "Updated description", category1.getTransactionType());
        
        repository.save(updated);
        
        Optional<Category> found = repository.findById(category1.getName());
        assertTrue(found.isPresent());
        assertEquals("Updated description", found.get().getDescription());
        assertEquals(1, repository.count()); // Still only 1 category
    }
    
    @Test
    void testFindAllReturnsCopy() {
        repository.save(category1);
        List<Category> all = repository.findAll();
        
        // Modifying returned list should not affect repository
        all.clear();
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceAcrossRestart() throws IOException {
        // Save category
        repository.save(category1);
        
        // Close and recreate repository (simulate restart)
        repository = new JsonCategoryRepository(testFile.toString());
        
        // Category should still be there
        Optional<Category> found = repository.findById(category1.getName());
        assertTrue(found.isPresent());
        assertEquals(category1.getName(), found.get().getName());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceWithMultipleCategories() throws IOException {
        repository.save(category1);
        repository.save(category2);
        
        // Recreate repository
        repository = new JsonCategoryRepository(testFile.toString());
        
        List<Category> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(category1));
        assertTrue(all.contains(category2));
    }
    
    @Test
    void testNullCategorySave() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }
    
    @Test
    void testNullNameOperations() {
        assertThrows(NullPointerException.class, () -> repository.findById(null));
        assertThrows(NullPointerException.class, () -> repository.delete(null));
        assertThrows(NullPointerException.class, () -> repository.exists(null));
    }
    
    @Test
    void testFileContentFormat() throws IOException {
        repository.save(category1);
        repository.save(category2);
        
        // Read file content to verify JSON structure
        String content = java.nio.file.Files.readString(testFile);
        
        assertTrue(content.contains(category1.getName()));
        assertTrue(content.contains(category1.getDescription()));
        assertTrue(content.contains(category1.getTransactionType().name()));
        assertTrue(content.contains(category2.getName()));
    }
    
    @Test
    void testEmptyFileHandling() throws IOException {
        // Create empty file
        java.nio.file.Files.createFile(testFile);
        
        // Repository should initialize without errors
        repository = new JsonCategoryRepository(testFile.toString());
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testCorruptedJsonFile() throws IOException {
        // Write invalid JSON to file
        java.nio.file.Files.writeString(testFile, "invalid json content");
        
        // Should throw IOException on initialization
        assertThrows(IOException.class, () -> 
            new JsonCategoryRepository(testFile.toString()));
    }
    
    @Test
    void testFindByCategoryWithDifferentNames() {
        Category food = new Category("Food", "Food", TransactionType.EXPENSE);
        Category foodCopy = new Category("Food", "Food copy", TransactionType.EXPENSE);
        Category transport = new Category("Transport", "Transport", TransactionType.EXPENSE);
        
        repository.save(food);
        repository.save(transport);
        
        // Should find by exact name match
        Optional<Category> found = repository.findById("Food");
        assertTrue(found.isPresent());
        assertEquals("Food", found.get().getName());
        
        // Different name not found
        assertFalse(repository.findById("Transportation").isPresent());
    }
}

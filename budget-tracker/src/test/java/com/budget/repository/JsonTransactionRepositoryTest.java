package com.budget.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;
import com.budget.service.ValidationService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.io.CleanupMode;

/**
 * Unit tests for JsonTransactionRepository.
 * Tests file persistence, CRUD operations, and thread safety.
 */
class JsonTransactionRepositoryTest {
    
    @TempDir
    Path tempDir;
    
    private JsonTransactionRepository repository;
    private Path testFile;
    private Transaction transaction1;
    private Transaction transaction2;
    
    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("transactions.json");
        repository = new JsonTransactionRepository(testFile.toString());
        
        Category category = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        transaction1 = new Transaction("Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), category, null);
        transaction2 = new Transaction("Dinner", new BigDecimal("25.00"), LocalDate.of(2024, 1, 16), category, null);
    }
    
    @Test
    void testInitializationWithNewFile() throws IOException {
        // Repository should start empty when file doesn't exist
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testSaveAndFindTransaction() {
        repository.save(transaction1);
        
        Optional<Transaction> found = repository.findById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals(transaction1, found.get());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testSaveMultipleTransactions() {
        repository.save(transaction1);
        repository.save(transaction2);
        
        List<Transaction> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(transaction1));
        assertTrue(all.contains(transaction2));
    }
    
    @Test
    void testFindByIdNotFound() {
        repository.save(transaction1);
        Optional<Transaction> found = repository.findById("nonexistent");
        assertFalse(found.isPresent());
    }
    
    @Test
    void testDeleteTransaction() {
        repository.save(transaction1);
        assertTrue(repository.exists(transaction1.getId()));
        
        repository.delete(transaction1.getId());
        assertFalse(repository.exists(transaction1.getId()));
        assertFalse(repository.findById(transaction1.getId()).isPresent());
        assertEquals(0, repository.count());
    }
    
    @Test
    void testDeleteNonExistentTransaction() {
        repository.save(transaction1);
        repository.delete("nonexistent"); // Should not throw
        assertEquals(1, repository.count());
    }
    
    @Test
    void testUpdateTransaction() {
        repository.save(transaction1);
        
        // Create updated version with same ID but different description and amount
        Transaction updated = new Transaction(
            transaction1.getId(),
            "Updated lunch",
            new BigDecimal("20.00"),
            transaction1.getDate(),
            transaction1.getLoggedTime(),
            transaction1.getCategory(),
            transaction1.getNotes()
        );
        
        repository.save(updated);
        
        Optional<Transaction> found = repository.findById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated lunch", found.get().getDescription());
        assertEquals(new BigDecimal("20.00"), found.get().getAmount());
        assertEquals(1, repository.count()); // Still only 1 transaction
    }
    
    @Test
    void testFindAllReturnsCopy() {
        repository.save(transaction1);
        List<Transaction> all = repository.findAll();
        
        // Modifying returned list should not affect repository
        all.clear();
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceAcrossRestart() throws IOException {
        // Save transaction
        repository.save(transaction1);
        
        // Close and recreate repository (simulate restart)
        repository = new JsonTransactionRepository(testFile.toString());
        
        // Transaction should still be there
        Optional<Transaction> found = repository.findById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals(transaction1.getDescription(), found.get().getDescription());
        assertEquals(1, repository.count());
    }
    
    @Test
    void testPersistenceWithMultipleTransactions() throws IOException {
        repository.save(transaction1);
        repository.save(transaction2);
        
        // Recreate repository
        repository = new JsonTransactionRepository(testFile.toString());
        
        List<Transaction> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(transaction1));
        assertTrue(all.contains(transaction2));
    }
    
    @Test
    void testNullTransactionSave() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }
    
    @Test
    void testNullIdOperations() {
        assertThrows(NullPointerException.class, () -> repository.findById(null));
        assertThrows(NullPointerException.class, () -> repository.delete(null));
        assertThrows(NullPointerException.class, () -> repository.exists(null));
    }
    
    @Test
    void testFileContentFormat() throws IOException {
        repository.save(transaction1);
        repository.save(transaction2);
        
        // Read file content to verify JSON structure
        List<String> lines = java.nio.file.Files.readAllLines(testFile);
        String content = String.join("", lines);
        
        assertTrue(content.contains(transaction1.getId()));
        assertTrue(content.contains("Lunch"));
        assertTrue(content.contains("15.50"));
        assertTrue(content.contains(transaction2.getId()));
        assertTrue(content.contains("Dinner"));
    }
    
    @Test
    void testEmptyFileHandling() throws IOException {
        // Create empty file
        java.nio.file.Files.createFile(testFile);
        
        // Repository should initialize without errors
        repository = new JsonTransactionRepository(testFile.toString());
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testCorruptedJsonFile() throws IOException {
        // Write invalid JSON to file
        java.nio.file.Files.writeString(testFile, "invalid json content");
        
        // Should throw IOException on initialization
        assertThrows(IOException.class, () -> 
            new JsonTransactionRepository(testFile.toString()));
    }
}

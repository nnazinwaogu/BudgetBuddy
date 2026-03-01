package com.budget.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.budget.model.Transaction;
import com.budget.model.Category;
import com.budget.model.TransactionType;

class InMemoryTransactionRepositoryTest {
    
    private InMemoryTransactionRepository repository;
    private Transaction transaction1;
    private Transaction transaction2;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        Category category = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        transaction1 = new Transaction("Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), category, null);
        transaction2 = new Transaction("Dinner", new BigDecimal("25.00"), LocalDate.of(2024, 1, 16), category, null);
    }
    
    @Test
    void testSaveAndFindTransaction() {
        repository.save(transaction1);
        Optional<Transaction> found = repository.findById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals(transaction1, found.get());
    }
    
    @Test
    void testFindByIdNotFound() {
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
    }
    
    @Test
    void testFindAllTransactions() {
        repository.save(transaction1);
        repository.save(transaction2);
        List<Transaction> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(transaction1));
        assertTrue(all.contains(transaction2));
    }
    
    @Test
    void testSaveNullTransaction() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }
    
    @Test
    void testFindByIdNull() {
        assertThrows(NullPointerException.class, () -> repository.findById(null));
    }
    
    @Test
    void testDeleteNull() {
        assertThrows(NullPointerException.class, () -> repository.delete(null));
    }
    
    @Test
    void testExistsNull() {
        assertThrows(NullPointerException.class, () -> repository.exists(null));
    }
    
    @Test
    void testSaveMultipleTransactions() {
        repository.save(transaction1);
        repository.save(transaction2);
        assertEquals(2, repository.findAll().size());
        assertTrue(repository.exists(transaction1.getId()));
        assertTrue(repository.exists(transaction2.getId()));
    }
    
    @Test
    void testDeleteNonExistentTransaction() {
        repository.save(transaction1);
        repository.delete("nonexistent");
        assertEquals(1, repository.findAll().size());
    }
}
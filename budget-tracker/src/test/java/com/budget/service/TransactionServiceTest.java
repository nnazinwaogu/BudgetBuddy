package com.budget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.budget.model.Transaction;
import com.budget.model.Category;
import com.budget.model.TransactionType;
import com.budget.repository.TransactionRepository;
import com.budget.repository.InMemoryTransactionRepository;

class TransactionServiceTest {
    
    private TransactionService service;
    private TransactionRepository repository;
    private Transaction transaction1;
    private Transaction transaction2;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        service = new TransactionService(repository);
        Category category = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        transaction1 = new Transaction("Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), category, null);
        transaction2 = new Transaction("Dinner", new BigDecimal("25.00"), LocalDate.of(2024, 1, 16), category, null);
    }
    
    @Test
    void testAddTransaction() {
        Transaction result = service.addTransaction(transaction1);
        assertEquals(transaction1, result);
        Optional<Transaction> found = service.getTransactionById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals(transaction1, found.get());
    }
    
    @Test
    void testGetAllTransactions() {
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        List<Transaction> all = service.getAllTransactions();
        assertEquals(2, all.size());
        assertTrue(all.contains(transaction1));
        assertTrue(all.contains(transaction2));
    }
    
    @Test
    void testGetTransactionById() {
        service.addTransaction(transaction1);
        Optional<Transaction> found = service.getTransactionById(transaction1.getId());
        assertTrue(found.isPresent());
        assertEquals(transaction1, found.get());
    }
    
    @Test
    void testGetTransactionByIdNotFound() {
        Optional<Transaction> found = service.getTransactionById("nonexistent");
        assertFalse(found.isPresent());
    }
    
    @Test
    void testDeleteTransaction() {
        service.addTransaction(transaction1);
        assertTrue(service.deleteTransaction(transaction1.getId()));
        assertFalse(service.getTransactionById(transaction1.getId()).isPresent());
    }
    
    @Test
    void testDeleteTransactionNotFound() {
        assertFalse(service.deleteTransaction("nonexistent"));
    }
    
    @Test
    void testFilterTransactions() {
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        
        List<Transaction> filtered = service.filterTransactions(t -> t.getAmount().compareTo(new BigDecimal("20")) > 0);
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(transaction2));
    }
    
    @Test
    void testFindTransactionsByDateRange() {
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        
        List<Transaction> range = service.findTransactionsByDateRange(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 16));
        assertEquals(2, range.size());
        assertTrue(range.contains(transaction1));
        assertTrue(range.contains(transaction2));
        
        List<Transaction> single = service.findTransactionsByDateRange(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 15));
        assertEquals(1, single.size());
        assertTrue(single.contains(transaction1));
    }
    
    @Test
    void testFindTransactionsByCategory() {
        Category category = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        Transaction foodTransaction = new Transaction("Lunch", new BigDecimal("15.50"), LocalDate.now(), category, null);
        Transaction otherTransaction = new Transaction("Movie", new BigDecimal("12.00"), LocalDate.now(), new Category("Entertainment", "Entertainment expenses", TransactionType.EXPENSE), null);
        
        service.addTransaction(foodTransaction);
        service.addTransaction(otherTransaction);
        
        List<Transaction> food = service.findTransactionsByCategory("Food");
        assertEquals(1, food.size());
        assertTrue(food.contains(foodTransaction));
    }
    
    @Test
    void testFindTransactionsByType() {
        Category expenseCategory = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        Category incomeCategory = new Category("Salary", "Monthly income", TransactionType.INCOME);
        
        Transaction expenseTransaction = new Transaction("Lunch", new BigDecimal("15.50"), LocalDate.now(), expenseCategory, null);
        Transaction incomeTransaction = new Transaction("Salary", new BigDecimal("1000"), LocalDate.now(), incomeCategory, null);
        
        service.addTransaction(expenseTransaction);
        service.addTransaction(incomeTransaction);
        
        List<Transaction> expenses = service.findTransactionsByType("EXPENSE");
        assertEquals(1, expenses.size());
        assertTrue(expenses.contains(expenseTransaction));
        
        List<Transaction> incomes = service.findTransactionsByType("INCOME");
        assertEquals(1, incomes.size());
        assertTrue(incomes.contains(incomeTransaction));
    }
    
    @Test
    void testNullInputs() {
        assertThrows(NullPointerException.class, () -> service.addTransaction(null));
        assertThrows(NullPointerException.class, () -> service.getTransactionById(null));
        assertThrows(NullPointerException.class, () -> service.deleteTransaction(null));
        assertThrows(NullPointerException.class, () -> service.filterTransactions(null));
        assertThrows(NullPointerException.class, () -> service.findTransactionsByDateRange(null, LocalDate.now()));
        assertThrows(NullPointerException.class, () -> service.findTransactionsByDateRange(LocalDate.now(), null));
        assertThrows(NullPointerException.class, () -> service.findTransactionsByCategory(null));
        assertThrows(NullPointerException.class, () -> service.findTransactionsByType(null));
    }
}
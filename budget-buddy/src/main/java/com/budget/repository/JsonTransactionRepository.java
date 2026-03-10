package com.budget.repository;

import com.budget.model.Transaction;
import com.budget.util.FileUtil;
import com.budget.util.JacksonJsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JSON file-based implementation of TransactionRepository.
 * Persists transactions to a JSON file and loads them on initialization.
 * Thread-safe with synchronized methods.
 */
public class JsonTransactionRepository implements TransactionRepository {
    
    private final String filePath;
    private final List<Transaction> transactions;
    
    /**
     * Constructs a JsonTransactionRepository with the specified file path.
     * Loads existing transactions from the file, or creates empty storage if file doesn't exist.
     *
     * @param filePath the path to the JSON file for storing transactions
     * @throws IOException if an I/O error occurs during loading
     */
    public JsonTransactionRepository(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        this.filePath = filePath;
        this.transactions = new ArrayList<>();
        
        // Load existing transactions if file exists
        if (FileUtil.fileExists(filePath)) {
            loadAll();
        }
    }
    
    /**
     * Loads all transactions from the JSON file into memory.
     * Clears current in-memory list and populates from file.
     *
     * @throws IOException if an I/O error occurs during reading
     */
    private synchronized void loadAll() throws IOException {
        List<Transaction> loaded = JacksonJsonUtil.readListFromFile(filePath, Transaction.class);
        transactions.clear();
        transactions.addAll(loaded);
    }
    
    /**
     * Saves all transactions to the JSON file.
     * Serializes the entire transaction list to JSON.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    private synchronized void saveAll() throws IOException {
        JacksonJsonUtil.writeListToFile(filePath, transactions);
    }
    
    @Override
    public synchronized void save(Transaction transaction) {
        if (transaction == null) {
            throw new NullPointerException("Transaction cannot be null");
        }
        
        // Remove existing transaction with same ID if it exists (update)
        transactions.removeIf(t -> t.getId().equals(transaction.getId()));
        
        // Add new transaction
        transactions.add(transaction);
        
        try {
            saveAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save transaction to file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public synchronized List<Transaction> findAll() {
        return new ArrayList<>(transactions); // Return copy for immutability
    }
    
    @Override
    public synchronized Optional<Transaction> findById(String id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public synchronized void delete(String id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        
        boolean removed = transactions.removeIf(t -> t.getId().equals(id));
        if (removed) {
            try {
                saveAll();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save after deletion to file: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public synchronized boolean exists(String id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        
        return transactions.stream().anyMatch(t -> t.getId().equals(id));
    }
    
    /**
     * Gets the total number of transactions in the repository.
     *
     * @return the count of transactions
     */
    public synchronized int count() {
        return transactions.size();
    }
}

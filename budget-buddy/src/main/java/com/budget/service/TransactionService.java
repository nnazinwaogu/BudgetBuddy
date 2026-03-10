package com.budget.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Predicate;

import java.time.LocalDate;
import java.util.Objects;

import com.budget.model.Transaction;
import com.budget.repository.TransactionRepository;

public class TransactionService {
    
    private final TransactionRepository repository;
    
    public TransactionService(TransactionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
    }
    
    public Transaction addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        repository.save(transaction);
        return transaction;
    }
    
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
    
    public Optional<Transaction> getTransactionById(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return repository.findById(id);
    }
    
    public boolean deleteTransaction(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        if (repository.exists(id)) {
            repository.delete(id);
            return true;
        }
        return false;
    }
    
    public List<Transaction> filterTransactions(Predicate<Transaction> filter) {
        Objects.requireNonNull(filter, "Filter cannot be null");
        return repository.findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
    
    public List<Transaction> findTransactionsByDateRange(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "Start date cannot be null");
        Objects.requireNonNull(end, "End date cannot be null");
        return filterTransactions(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end));
    }
    
    public List<Transaction> findTransactionsByCategory(String categoryName) {
        Objects.requireNonNull(categoryName, "Category name cannot be null");
        return filterTransactions(t -> t.getCategory().getName().equals(categoryName));
    }
    
    public List<Transaction> findTransactionsByType(String type) {
        Objects.requireNonNull(type, "Type cannot be null");
        return filterTransactions(t -> t.getCategory().getTransactionType().name().equals(type.toUpperCase()));
    }
}
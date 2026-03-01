package com.budget.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Objects;

import com.budget.model.Transaction;

public class InMemoryTransactionRepository implements TransactionRepository {
    
    private final Map<String, Transaction> transactions = new HashMap<>();
    private final List<Transaction> transactionList = new ArrayList<>();
    
    @Override
    public synchronized void save(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        transactions.put(transaction.getId(), transaction);
        transactionList.add(transaction);
    }
    
    @Override
    public synchronized List<Transaction> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(transactionList));
    }
    
    @Override
    public synchronized Optional<Transaction> findById(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return Optional.ofNullable(transactions.get(id));
    }
    
    @Override
    public synchronized void delete(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Transaction removed = transactions.remove(id);
        if (removed != null) {
            transactionList.remove(removed);
        }
    }
    
    @Override
    public synchronized boolean exists(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return transactions.containsKey(id);
    }
}
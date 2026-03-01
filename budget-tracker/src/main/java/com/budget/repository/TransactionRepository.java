package com.budget.repository;

import java.util.List;
import java.util.Optional;

import com.budget.model.Transaction;

public interface TransactionRepository {
    
    void save(Transaction transaction);
    
    List<Transaction> findAll();
    
    Optional<Transaction> findById(String id);
    
    void delete(String id);
    
    boolean exists(String id);
}
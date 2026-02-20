package com.budget.model;

public enum TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER;
    
    public static boolean isValidType(String type) {
        if (type == null) {
            return false;
        }
        
        try {
            TransactionType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static TransactionType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be null or empty");
        }
        
        try {
            return TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }
}
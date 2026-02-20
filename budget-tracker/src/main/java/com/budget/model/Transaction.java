package com.budget.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Transaction {
    private final String id;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final LocalDateTime loggedTime;
    private final Category category;
    private final String notes;
    
    public Transaction(String description, BigDecimal amount, LocalDate date, Category category, String notes) {
        //Pre-defined values
        this.id = UUID.randomUUID().toString(); 
        this.loggedTime = LocalDateTime.now(); 

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        if (loggedTime == null) {
            throw new IllegalArgumentException("Transaction logged time cannot be null");
        }
       
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction description cannot be null or empty");
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException("Transaction description cannot exceed 200 characters");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        if (date == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Transaction category cannot be null");
        }
        if (notes != null && notes.length() > 500) {
            throw new IllegalArgumentException("Transaction notes cannot exceed 500 characters");
        }
    
        this.description = description.trim();
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.notes = notes != null ? notes.trim() : null;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getLoggedTime() {
        return loggedTime;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public String getNotes() {
        return notes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Transaction{id='" + id + "', description='" + description + "', amount=" + amount + 
               ", date=" + date + ", loggedTime=" + loggedTime + ", category=" + category + ", notes='" + notes + "'}";
    }
}
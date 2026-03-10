package com.budget.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.budget.service.ValidationService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Transaction {
    private final String id;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final LocalDateTime loggedTime;
    private final Category category;
    private final String notes;
    
    /**
     * Primary constructor for creating new transactions.
     * Generates a new UUID and sets loggedTime to current time.
     */
    public Transaction(String description, BigDecimal amount, LocalDate date, Category category, String notes) {
        this(UUID.randomUUID().toString(), description, amount, date, LocalDateTime.now(), category, notes);
    }
    
    /**
     * Jackson deserialization constructor.
     * Used to reconstruct Transaction from JSON with all fields.
     */
    @JsonCreator
    public Transaction(
        @JsonProperty("id") String id,
        @JsonProperty("description") String description,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("date") LocalDate date,
        @JsonProperty("loggedTime") LocalDateTime loggedTime,
        @JsonProperty("category") Category category,
        @JsonProperty("notes") String notes) {
        
        // Validate using ValidationService
        ValidationService.validateTransactionId(id);
        ValidationService.validateTransactionLoggedTime(loggedTime);
        ValidationService.validateTransactionDescription(description);
        ValidationService.validateTransactionAmount(amount);
        ValidationService.validateTransactionDate(date);
        ValidationService.validateTransactionCategory(category);
        ValidationService.validateTransactionNotes(notes);
    
        this.id = id;
        this.description = description.trim();
        this.amount = amount;
        this.date = date;
        this.loggedTime = loggedTime;
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
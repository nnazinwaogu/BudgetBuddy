package com.budget;

import com.budget.model.Category;
import com.budget.model.TransactionType;
import com.budget.repository.InMemoryTransactionRepository;
import com.budget.service.TransactionService;
import com.budget.cli.BudgetTrackerCLI;

/**
 * BudgetTracker Application Entry Point
 * 
 * This is the main application class that starts the CLI-based budget tracking system.
 * It initializes the application components and launches the interactive menu system.
 */
public class App 
{
    public static void main( String[] args )
    {
        // Initialize repository and service layer
        InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
        TransactionService transactionService = new TransactionService(repository);
        
        // Initialize and start CLI
        BudgetTrackerCLI cli = new BudgetTrackerCLI(transactionService);
        cli.start();
    }
}

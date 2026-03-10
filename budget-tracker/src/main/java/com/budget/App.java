package com.budget;

import com.budget.repository.InMemoryTransactionRepository;
import com.budget.repository.JsonTransactionRepository;
import com.budget.service.TransactionService;
import com.budget.cli.BudgetTrackerCLI;
import com.budget.util.FileUtil;

import java.io.IOException;

/**
 * BudgetTracker Application Entry Point
 * 
 * This is the main application class that starts the CLI-based budget tracking system.
 * It initializes the application components and launches the interactive menu system.
 * Uses JSON file persistence by default, with fallback to in-memory storage.
 */
public class App 
{
    private static final String DATA_DIR = "data";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.json";
    
    public static void main( String[] args )
    {
        try {
            // Ensure data directory exists
            FileUtil.ensureDirectoryExists(DATA_DIR);
            
            // Try to use JSON persistence
            JsonTransactionRepository repository = new JsonTransactionRepository(TRANSACTIONS_FILE);
            TransactionService transactionService = new TransactionService(repository);
            
            System.out.println("BudgetTracker started with JSON persistence.");
            System.out.println("Data stored in: " + FileUtil.getAbsolutePath(TRANSACTIONS_FILE));
            
            // Initialize and start CLI
            BudgetTrackerCLI cli = new BudgetTrackerCLI(transactionService);
            cli.start();
            
        } catch (IOException e) {
            System.err.println("Warning: Could not initialize JSON storage: " + e.getMessage());
            System.err.println("Falling back to in-memory storage (data will not be saved).");
            
            // Fallback to in-memory repository
            InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
            TransactionService transactionService = new TransactionService(repository);
            
            BudgetTrackerCLI cli = new BudgetTrackerCLI(transactionService);
            cli.start();
        } catch (Exception e) {
            System.err.println("Fatal error starting application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

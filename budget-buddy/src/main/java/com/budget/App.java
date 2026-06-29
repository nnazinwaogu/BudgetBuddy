package com.budget;

import com.budget.repository.InMemoryBudgetRepository;
import com.budget.repository.InMemoryTransactionRepository;
import com.budget.repository.JsonBudgetRepository;
import com.budget.repository.JsonTransactionRepository;
import com.budget.service.BudgetService;
import com.budget.service.TransactionService;
import com.budget.cli.BudgetBuddyCLI;
import com.budget.util.FileUtil;

import java.io.IOException;

/**
     * BudgetBuddy Application Entry Point
 *
 * This is the main application class that starts the CLI-based budget tracking system.
 * It initializes the application components and launches the interactive menu system.
 * Uses JSON file persistence by default, with fallback to in-memory storage.
 */
public class App
{
    private static final String DATA_DIR = "data";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.json";
    private static final String BUDGETS_FILE = DATA_DIR + "/budgets.json";

    public static void main( String[] args )
    {
        try {
            // Ensure data directory exists
            FileUtil.ensureDirectoryExists(DATA_DIR);

            // Try to use JSON persistence
            JsonTransactionRepository transactionRepo = new JsonTransactionRepository(TRANSACTIONS_FILE);
            TransactionService transactionService = new TransactionService(transactionRepo);

            JsonBudgetRepository budgetRepo = new JsonBudgetRepository(BUDGETS_FILE);
            BudgetService budgetService = new BudgetService(budgetRepo, transactionRepo);

            System.out.println("BudgetBuddy started with JSON persistence.");
            System.out.println("Data stored in: " + FileUtil.getAbsolutePath(TRANSACTIONS_FILE));

            // Initialize and start CLI
            BudgetBuddyCLI cli = new BudgetBuddyCLI(transactionService, budgetService);
            cli.start();

        } catch (IOException e) {
            System.err.println("Warning: Could not initialize JSON storage: " + e.getMessage());
            System.err.println("Falling back to in-memory storage (data will not be saved).");

            // Fallback to in-memory repository
            InMemoryTransactionRepository transactionRepo = new InMemoryTransactionRepository();
            TransactionService transactionService = new TransactionService(transactionRepo);

            InMemoryBudgetRepository budgetRepo = new InMemoryBudgetRepository();
            BudgetService budgetService = new BudgetService(budgetRepo, transactionRepo);

            BudgetBuddyCLI cli = new BudgetBuddyCLI(transactionService, budgetService);
            cli.start();
        } catch (Exception e) {
            System.err.println("Fatal error starting application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

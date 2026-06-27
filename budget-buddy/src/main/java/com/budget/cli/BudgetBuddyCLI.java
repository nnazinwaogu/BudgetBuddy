package com.budget.cli;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.ArrayList;
import java.time.Month;
import java.util.stream.Collectors;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;
import com.budget.service.BudgetService;
import com.budget.service.ExportService;
import com.budget.service.TransactionService;
import com.budget.service.ValidationService;
import com.budget.util.FileUtil;

public class BudgetBuddyCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int BAR_WIDTH = 30;
    private static final String DEFAULT_EXPORT_DIR = "data";

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final ExportService exportService;

    public BudgetBuddyCLI(TransactionService transactionService, BudgetService budgetService, ExportService exportService) {
        this.transactionService = transactionService;
        this.budgetService = budgetService;
        this.exportService = exportService;
    }

    public void start() {
        System.out.println("=== BudgetBuddy CLI ===");
        System.out.println("Personal Budget & Expense Management System");
        System.out.println("==============================");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addTransaction();
                        break;
                    case 2:
                        viewAllTransactions();
                        break;
                    case 3:
                        filterTransactions();
                        break;
                    case 4:
                        generateReport();
                        break;
                    case 5:
                        budgetManagement();
                        break;
                    case 6:
                        exportDataMenu();
                        break;
                    case 7:
                        running = false;
                        System.out.println("Thank you for using BudgetBuddy!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                try {
                    scanner.nextLine();
                } catch (Exception ex) {
                    // Ignore if no input available
                }
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Transaction");
        System.out.println("2. View All Transactions");
        System.out.println("3. Filter Transactions");
        System.out.println("4. Generate Report");
        System.out.println("5. Budget Management");
        System.out.println("6. Export Data");
        System.out.println("7. Exit");
    }

    private void addTransaction() {
        System.out.println("\n=== Add Transaction ===");

        String description = readString("Description: ", 200);
        BigDecimal amount = readBigDecimal("Amount: ");
        LocalDate date = readDate("Date (yyyy-MM-dd): ");
        TransactionType type = readTransactionType();
        Category category = readCategory(type);
        String notes = readOptionalString("Notes (optional): ", 500);

        Category transactionCategory = new Category(category.getName(), category.getDescription(), type);
        Transaction transaction = new Transaction(description, amount, date, transactionCategory, notes);

        transactionService.addTransaction(transaction);
        System.out.println("Transaction added successfully!");
        System.out.println("Transaction ID: " + transaction.getId());
    }

    private void viewAllTransactions() {
        System.out.println("\n=== All Transactions ===");

        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println(formatTransaction(transaction));
        }

        System.out.println("\nTotal transactions: " + transactions.size());
    }

    private void filterTransactions() {
        System.out.println("\n=== Filter Transactions ===");
        System.out.println("1. By Date Range");
        System.out.println("2. By Category");
        System.out.println("3. By Type");
        System.out.println("4. Go Back");

        int choice = readInt("Enter your choice: ");

        switch (choice) {
            case 1:
                filterByDateRange();
                break;
            case 2:
                filterByCategory();
                break;
            case 3:
                filterByType();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void filterByDateRange() {
        System.out.println("\n=== Filter by Date Range ===");
        LocalDate startDate = readDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("End date (yyyy-MM-dd): ");

        List<Transaction> transactions = transactionService.findTransactionsByDateRange(startDate, endDate);
        displayFilteredTransactions(transactions, "Transactions from " + startDate + " to " + endDate);
    }

    private void filterByCategory() {
        System.out.println("\n=== Filter by Category ===");
        TransactionType type = readTransactionType();
        Category category = readCategory(type);

        List<Transaction> transactions = transactionService.findTransactionsByCategory(category.getName());
        displayFilteredTransactions(transactions, "Transactions in category: " + category.getName());
    }

    private void filterByType() {
        System.out.println("\n=== Filter by Type ===");
        TransactionType type = readTransactionType();

        List<Transaction> transactions = transactionService.findTransactionsByType(type.name());
        displayFilteredTransactions(transactions, "Transactions of type: " + type.name());
    }

    private void generateReport() {
        System.out.println("\n=== Generate Report ===");
        System.out.println("1. Income vs Expense Summary");
        System.out.println("2. Monthly Expense Breakdown");
        System.out.println("3. Category-wise Expense Report");
        System.out.println("4. Month-over-Month Comparison");
        System.out.println("5. Year-over-Year Comparison");
        System.out.println("6. Category Trend Analysis");
        System.out.println("7. Go Back");

        int choice = readInt("Enter your choice: ");

        switch (choice) {
            case 1:
                generateIncomeExpenseSummary();
                break;
            case 2:
                generateMonthlyBreakdown();
                break;
            case 3:
                generateCategoryReport();
                break;
            case 4:
                generateMonthOverMonthReport();
                break;
            case 5:
                generateYearOverYearReport();
                break;
            case 6:
                generateCategoryTrendReport();
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void generateIncomeExpenseSummary() {
        System.out.println("\n=== Income vs Expense Summary ===");

        List<Transaction> allTransactions = transactionService.getAllTransactions();

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalTransfer = BigDecimal.ZERO;

        for (Transaction t : allTransactions) {
            switch (t.getCategory().getTransactionType()) {
                case INCOME:  totalIncome = totalIncome.add(t.getAmount()); break;
                case EXPENSE: totalExpense = totalExpense.add(t.getAmount()); break;
                case TRANSFER: totalTransfer = totalTransfer.add(t.getAmount()); break;
            }
        }

        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expense: $" + totalExpense);
        System.out.println("Total Transfer: $" + totalTransfer);

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal netBalance = totalIncome.subtract(totalExpense);
            System.out.println("Net Balance: $" + netBalance);

            if (netBalance.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("You're in surplus!");
            } else if (netBalance.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("You're in deficit!");
            } else {
                System.out.println("Your income matches your expenses.");
            }
        }
    }

    private void generateMonthlyBreakdown() {
        System.out.println("\n=== Monthly Expense Breakdown ===");

        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());

        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }

        Map<String, BigDecimal> monthlyTotals = new TreeMap<>();
        for (Transaction t : expenses) {
            String monthKey = t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue());
            monthlyTotals.merge(monthKey, t.getAmount(), BigDecimal::add);
        }

        printBarChart(monthlyTotals, "Month", "Total");
    }

    private void generateCategoryReport() {
        System.out.println("\n=== Category-wise Expense Report ===");

        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());

        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }

        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Transaction t : expenses) {
            categoryTotals.merge(t.getCategory().getName(), t.getAmount(), BigDecimal::add);
        }

        printBarChart(categoryTotals, "Category", "Total");
    }

    private void generateYearOverYearReport() {
        System.out.println("\n=== Year-over-Year Comparison ===");

        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());

        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }

        int month = readInt("Enter month (1-12): ");
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please enter a value between 1 and 12.");
            return;
        }

        String monthName = Month.of(month).name();
        System.out.println("\nMonth: " + monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase());

        Map<Integer, BigDecimal> yearlyTotals = new TreeMap<>();
        for (Transaction t : expenses) {
            if (t.getDate().getMonthValue() == month) {
                yearlyTotals.merge(t.getDate().getYear(), t.getAmount(), BigDecimal::add);
            }
        }

        if (yearlyTotals.isEmpty()) {
            System.out.println("No expenses found for month " + month + ".");
            return;
        }

        BigDecimal max = yearlyTotals.values().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        // Track years for change calculation
        List<Integer> years = new ArrayList<>(yearlyTotals.keySet());
        Map<Integer, String> changeMap = new HashMap<>();

        for (int i = 1; i < years.size(); i++) {
            int prevYear = years.get(i - 1);
            int currYear = years.get(i);
            BigDecimal prevTotal = yearlyTotals.get(prevYear);
            BigDecimal currTotal = yearlyTotals.get(currYear);
            BigDecimal diff = currTotal.subtract(prevTotal);

            String pctChange = prevTotal.compareTo(BigDecimal.ZERO) > 0
                ? diff.multiply(BigDecimal.valueOf(100)).divide(prevTotal, 1, RoundingMode.HALF_UP).toString()
                : "N/A";

            // Show first year as baseline with a starting indicator
            if (i == 1) {
                changeMap.put(prevYear, "  –");
            }       
            String arrow = diff.compareTo(BigDecimal.ZERO) > 0 ? "▲"
                : diff.compareTo(BigDecimal.ZERO) < 0 ? "▼" : "–";
            String sign = diff.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
            changeMap.put(currYear, "  " + arrow + " " + sign + "$" + diff.setScale(2, RoundingMode.HALF_UP) + " (" + pctChange + "%)");
        }

        for (Map.Entry<Integer, BigDecimal> entry : yearlyTotals.entrySet()) {
            int year = entry.getKey();
            BigDecimal value = entry.getValue().setScale(2, RoundingMode.HALF_UP);

            String bar = renderBar(value, max);
            String change = changeMap.getOrDefault(year, "");
            System.out.printf("%-6s $%-8s %s%s%n", year + ":", value, bar, change);
        }
    }

    private void generateMonthOverMonthReport() {
        System.out.println("\n=== Month-over-Month Comparison ===");

        int year = readInt("Enter year (e.g., 2024): ");
        if (year < 1) {
            System.out.println("Invalid year.");
            return;
        }

        List<Transaction> expenses = transactionService.getAllTransactions().stream()
            .filter(t -> t.getCategory().getTransactionType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());

        if (expenses.isEmpty()) {
            System.out.println("No expense transactions found.");
            return;
        }

        // Collect monthly totals for the selected year
        Map<Integer, BigDecimal> monthlyTotals = new TreeMap<>();
        for (Transaction t : expenses) {
            if (t.getDate().getYear() == year) {
                monthlyTotals.merge(t.getDate().getMonthValue(), t.getAmount(), BigDecimal::add);
            }
        }

        if (monthlyTotals.isEmpty()) {
            System.out.println("No expenses found for year " + year + ".");
            return;
        }

        // Fill in missing months with zero
        for (int m = 1; m <= 12; m++) {
            monthlyTotals.putIfAbsent(m, BigDecimal.ZERO);
        }

        BigDecimal max = monthlyTotals.values().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        List<Integer> months = new ArrayList<>(monthlyTotals.keySet());

        for (int i = 0; i < months.size(); i++) {
            int month = months.get(i);
            BigDecimal value = monthlyTotals.get(month).setScale(2, RoundingMode.HALF_UP);

            String monthName = Month.of(month).name();
            String shortMonth = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3).toLowerCase();

            String bar = renderBar(value, max);

            // Calculate change from previous month
            String change = "";
            if (i > 0) {
                int prevMonth = months.get(i - 1);
                BigDecimal prevValue = monthlyTotals.get(prevMonth);
                BigDecimal diff = value.subtract(prevValue);

                // Only show change indicator if there was activity in at least one of the months
                if (prevValue.compareTo(BigDecimal.ZERO) != 0 || value.compareTo(BigDecimal.ZERO) != 0) {
                    String pct = prevValue.compareTo(BigDecimal.ZERO) > 0
                        ? diff.multiply(BigDecimal.valueOf(100)).divide(prevValue, 1, RoundingMode.HALF_UP).toString()
                        : (diff.compareTo(BigDecimal.ZERO) > 0 ? "+∞" : "0.0");

                    String arrow = diff.compareTo(BigDecimal.ZERO) > 0 ? "▲"
                        : diff.compareTo(BigDecimal.ZERO) < 0 ? "▼" : "–";
                    String sign = diff.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
                    change = "  " + arrow + " " + sign + "$" + diff.setScale(2, RoundingMode.HALF_UP) + " (" + pct + "%)";
                }
            }

            System.out.printf("%-6s $%-8s %s%s%n", shortMonth + ":", value, bar, change);
        }

        // Calculate and display average monthly spending
        BigDecimal total = monthlyTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = total.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        System.out.println("\nAverage monthly spending: $" + avg);
    }

    private void generateCategoryTrendReport() {
        System.out.println("\n=== Category Trend Analysis ===");

        // Only expense categories make sense for spending trends
        TransactionType type = TransactionType.EXPENSE;
        Category category = readCategory(type);

        List<Transaction> matching = transactionService.findTransactionsByCategory(category.getName());
        if (matching.isEmpty()) {
            System.out.println("No transactions found for category: " + category.getName());
            return;
        }

        // Group by YearMonth (only expense-type transactions for this category name)
        Map<YearMonth, BigDecimal> monthlyTotals = new TreeMap<>();
        for (Transaction t : matching) {
            if (t.getCategory().getTransactionType() == TransactionType.EXPENSE) {
                monthlyTotals.merge(YearMonth.from(t.getDate()), t.getAmount(), BigDecimal::add);
            }
        }

        if (monthlyTotals.isEmpty()) {
            System.out.println("No expense transactions found for category: " + category.getName());
            return;
        }

        // Default to last 12 months, but show fewer if not enough data
        int count = monthlyTotals.size();
        int displayMonths = Math.min(count, 12);

        System.out.println("\nCategory: " + category.getName() + " (last " + displayMonths + " month(s))");

        // Take only the most recent months
        List<Map.Entry<YearMonth, BigDecimal>> entries = new ArrayList<>(monthlyTotals.entrySet());
        List<Map.Entry<YearMonth, BigDecimal>> recent = entries.subList(Math.max(0, entries.size() - displayMonths), entries.size());

        BigDecimal max = recent.stream().map(Map.Entry::getValue).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        for (int i = 0; i < recent.size(); i++) {
            YearMonth ym = recent.get(i).getKey();
            BigDecimal value = recent.get(i).getValue().setScale(2, RoundingMode.HALF_UP);

            String bar = renderBar(value, max);
            String label = ym.getMonth().name().substring(0, 1).toUpperCase()
                + ym.getMonth().name().substring(1, 3).toLowerCase()
                + " " + ym.getYear();

            String change = "";
            // Show first yearMonth as baseline with a starting indicator
            if (i == 0) {
                change = "  –";
            }
            if (i > 0) {
                BigDecimal prevValue = recent.get(i - 1).getValue();
                BigDecimal diff = value.subtract(prevValue);

                if (prevValue.compareTo(BigDecimal.ZERO) != 0 || value.compareTo(BigDecimal.ZERO) != 0) {
                    String pct = prevValue.compareTo(BigDecimal.ZERO) > 0
                        ? diff.multiply(BigDecimal.valueOf(100)).divide(prevValue, 1, RoundingMode.HALF_UP).toString()
                        : (diff.compareTo(BigDecimal.ZERO) > 0 ? "+∞" : "0.0");

                    String arrow = diff.compareTo(BigDecimal.ZERO) > 0 ? "▲"
                        : diff.compareTo(BigDecimal.ZERO) < 0 ? "▼" : "–";
                    String sign = diff.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
                    change = "  " + arrow + " " + sign + "$" + diff.setScale(2, RoundingMode.HALF_UP) + " (" + pct + "%)";
                }
            }

            System.out.printf("%-10s $%-8s %s%s%n", label + ":", value, bar, change);
        }

        BigDecimal total = recent.stream().map(Map.Entry::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = total.divide(BigDecimal.valueOf(recent.size()), 2, RoundingMode.HALF_UP);
        System.out.println("\nAverage: $" + avg + "/month");
    }

    private void printBarChart(Map<String, BigDecimal> data, String labelHeader, String valueHeader) {
        if (data.isEmpty()) return;

        System.out.printf("%-12s %-10s%n", labelHeader + ":", valueHeader);

        BigDecimal max = data.values().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        for (Map.Entry<String, BigDecimal> entry : data.entrySet()) {
            String label = entry.getKey();
            BigDecimal value = entry.getValue().setScale(2, RoundingMode.HALF_UP);

            String bar = renderBar(value, max);
            String pct = max.compareTo(BigDecimal.ZERO) > 0
                ? value.multiply(BigDecimal.valueOf(100)).divide(max, 1, RoundingMode.HALF_UP).toString() + "%"
                : "0.0%";

            System.out.printf("%-12s $%-8s %s %s%n", label + ":", value, bar, pct);
        }
    }

    private String renderBar(BigDecimal value, BigDecimal max) {
        int barLength = max.compareTo(BigDecimal.ZERO) > 0
            ? value.multiply(BigDecimal.valueOf(BAR_WIDTH)).divide(max, 0, RoundingMode.HALF_UP).intValue()
            : 0;
        return "█".repeat(Math.max(0, barLength));
    }

    private void budgetManagement() {
        System.out.println("\n=== Budget Management ===");

        boolean inBudgetMenu = true;
        while (inBudgetMenu) {
            System.out.println("\n--- Budget Menu ---");
            System.out.println("1. Set Budget per Category");
            System.out.println("2. View Budget Status");
            System.out.println("3. View Spending vs Budget Report");
            System.out.println("4. Go Back");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        setBudget();
                        break;
                    case 2:
                        viewBudgetStatus();
                        break;
                    case 3:
                        viewSpendingReport();
                        break;
                    case 4:
                        inBudgetMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void setBudget() {
        System.out.println("\n--- Set Budget per Category ---");
        System.out.println("Enter category details for the budget (expense categories only):");

        TransactionType type = TransactionType.EXPENSE;
        Category category = readCategory(type);

        if (budgetService.getBudgetByCategory(category).isPresent()) {
            System.out.println("Budget already exists for category '" + category.getName() + "'.");
            System.out.print("Would you like to update it? (y/n): ");
            String response = scanner.nextLine().trim();
            if (!response.equalsIgnoreCase("y")) {
                return;
            }
        }

        BigDecimal limit = readBigDecimal("Monthly limit: ");
        Budget budget = new Budget(category, limit);
        budgetService.saveBudget(budget);
        System.out.println("Budget set successfully for " + category.getName() + ": $" + limit + "/month");
    }

    private void viewBudgetStatus() {
        System.out.println("\n--- Budget Status ---");

        List<Budget> budgets = budgetService.getAllBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets set. Use 'Set Budget per Category' first.");
            return;
        }

        YearMonth currentMonth = YearMonth.now();
        System.out.printf("%-20s %-15s %-15s %-15s %s%n",
            "Category", "Limit", "Spent", "Used", "Status");
        System.out.println(String.format("%100s", "").replace(' ', '-'));

        for (Budget budget : budgets) {
            Category category = budget.getCategory();
            try {
                BudgetService.BudgetStatus status = budgetService.getBudgetStatus(category, currentMonth);
                String statusLabel;
                if (status.percentageUsed().compareTo(new BigDecimal("100.00")) >= 0) {
                    statusLabel = "EXCEEDED!";
                } else if (status.percentageUsed().compareTo(new BigDecimal("80.00")) > 0) {
                    statusLabel = "OVER 80%!";
                } else {
                    statusLabel = "OK";
                }
                System.out.printf("%-20s $%-13s $%-13s %-13s %s%n",
                    category.getName(),
                    status.limit(),
                    status.spending(),
                    status.percentageUsed() + "%",
                    statusLabel);
            } catch (IllegalArgumentException e) {
                System.out.printf("%-20s $%-13s %-15s %-15s %s%n",
                    category.getName(),
                    budget.getMonthlyLimit(),
                    "N/A", "N/A", "No data");
            }
        }
    }

    private void viewSpendingReport() {
        System.out.println("\n=== Spending vs Budget Report ===");

        List<Budget> budgets = budgetService.getAllBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets set. Use 'Set Budget per Category' first.");
            return;
        }

        YearMonth currentMonth = YearMonth.now();
        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;

        System.out.printf("%-20s %-15s %-15s %-15s %-15s%n",
            "Category", "Budget", "Spent", "Remaining", "Used %");
        System.out.println(String.format("%100s", "").replace(' ', '-'));

        for (Budget budget : budgets) {
            Category category = budget.getCategory();
            try {
                BudgetService.BudgetStatus status = budgetService.getBudgetStatus(category, currentMonth);
                BigDecimal remaining = status.limit().subtract(status.spending());
                totalBudget = totalBudget.add(status.limit());
                totalSpent = totalSpent.add(status.spending());

                String warning = "";
                if (status.percentageUsed().compareTo(new BigDecimal("80.00")) > 0) {
                    warning = " *** WARNING: Approaching limit!";
                }
                System.out.printf("%-20s $%-13s $%-13s $%-13s %-13s%s%n",
                    category.getName(),
                    status.limit(),
                    status.spending(),
                    remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining,
                    status.percentageUsed() + "%",
                    warning);
            } catch (IllegalArgumentException e) {
                System.out.println(category.getName() + ": Unable to calculate spending.");
            }
        }

        System.out.println(String.format("%100s", "").replace(' ', '-'));
        System.out.printf("%-20s $%-13s $%-13s $%-13s%n",
            "TOTAL", totalBudget, totalSpent, totalBudget.subtract(totalSpent));
    }

    private void exportDataMenu() {
        System.out.println("\n=== Export Data ===");

        boolean inExportMenu = true;
        while (inExportMenu) {
            System.out.println("\n--- Export Menu ---");
            System.out.println("1. Export All Transactions");
            System.out.println("2. Export Filtered Transactions");
            System.out.println("3. Go Back");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        exportAllTransactions();
                        break;
                    case 2:
                        exportFilteredTransactions();
                        break;
                    case 3:
                        inExportMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void exportAllTransactions() {
        System.out.println("\n--- Export All Transactions ---");

        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions to export.");
            return;
        }

        System.out.println("Found " + transactions.size() + " transaction(s) to export.");
        System.out.print("Enter output folder [" + DEFAULT_EXPORT_DIR + "]: ");
        String folderPath = scanner.nextLine().trim();
        if (folderPath.isEmpty()) {
            folderPath = DEFAULT_EXPORT_DIR;
        }
        exportToFolder(folderPath, transactions, "all_transactions.csv");
    }

    private void exportFilteredTransactions() {
        System.out.println("\n--- Export Filtered Transactions ---");
        System.out.println("1. By Date Range");
        System.out.println("2. By Category");
        System.out.println("3. By Type");
        System.out.println("4. Go Back");

        int choice = readInt("Enter your choice: ");

        List<Transaction> filtered;
        String suggestedName;

        switch (choice) {
            case 1: {
                System.out.println("\n--- Filter by Date Range ---");
                LocalDate startDate = readDate("Start date (yyyy-MM-dd): ");
                LocalDate endDate = readDate("End date (yyyy-MM-dd): ");
                filtered = transactionService.findTransactionsByDateRange(startDate, endDate);
                suggestedName = "transactions_" + startDate + "_to_" + endDate + ".csv";
                break;
            }
            case 2: {
                System.out.println("\n--- Filter by Category ---");
                TransactionType type = readTransactionType();
                Category category = readCategory(type);
                filtered = transactionService.findTransactionsByCategory(category.getName()).stream()
                    .filter(t -> t.getCategory().getTransactionType() == type)
                    .collect(Collectors.toList());
                String safeName = category.getName().replaceAll("[^a-zA-Z0-9 _-]", "");
                suggestedName = "transactions_" + safeName + ".csv";
                break;
            }
            case 3: {
                System.out.println("\n--- Filter by Type ---");
                TransactionType type = readTransactionType();
                filtered = transactionService.findTransactionsByType(type.name());
                suggestedName = "transactions_" + type.name() + ".csv";
                break;
            }
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (filtered == null) {
            return;
        }

        displayFilteredTransactions(filtered, "Filtered Transactions");

        if (!filtered.isEmpty()) {
            System.out.print("\nExport these results to CSV? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                System.out.print("Enter output folder [" + DEFAULT_EXPORT_DIR + "]: ");
                String folderPath = scanner.nextLine().trim();
                if (folderPath.isEmpty()) {
                    folderPath = DEFAULT_EXPORT_DIR;
                }
                exportToFolder(folderPath, filtered, suggestedName);
            }
        }
    }

    private void exportToFolder(String folderPath, List<Transaction> transactions, String filename) {
        String normalized = folderPath.replaceAll("[/\\\\]$", "");
        String fullPath = normalized + "/" + filename;
        try {
            FileUtil.ensureDirectoryExists(fullPath);
            exportService.exportToCsv(transactions, fullPath);
            System.out.println("Successfully exported " + transactions.size() + " transaction(s) to " + FileUtil.getAbsolutePath(fullPath));
        } catch (IOException e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
        }
    }

    private void displayFilteredTransactions(List<Transaction> transactions, String title) {
        System.out.println("\n" + title + " ===");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println(formatTransaction(transaction));
        }

        System.out.println("\nTotal transactions: " + transactions.size());
    }

    private String formatTransaction(Transaction transaction) {
        return String.format(
            "ID: %s | %s | %.2f | %s | %s | %s",
            transaction.getId(),
            transaction.getDescription(),
            transaction.getAmount(),
            DATE_FORMATTER.format(transaction.getDate()),
            transaction.getCategory().getName(),
            transaction.getCategory().getTransactionType()
        );
    }

    String readString(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                ValidationService.validateString(input, "Input", maxLength);
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    String readOptionalString(String prompt, int maxLength) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            ValidationService.validateLength(input, "Input", maxLength);
            return input;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return readOptionalString(prompt, maxLength);
        }
    }

    BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(input);
                ValidationService.validateGreaterThanZero(amount, "Amount");
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                ValidationService.validateTransactionDate(date);
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter date in format yyyy-MM-dd.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NoSuchElementException e) {
                System.out.println("\nError: No input available. Please run the application from a terminal with input capabilities.");
                System.exit(1);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    TransactionType readTransactionType() {
        while (true) {
            System.out.println("\nSelect transaction type:");
            System.out.println("1. Income");
            System.out.println("2. Expense");
            System.out.println("3. Transfer");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    return TransactionType.INCOME;
                case 2:
                    return TransactionType.EXPENSE;
                case 3:
                    return TransactionType.TRANSFER;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    Category readCategory(TransactionType type) {
        while (true) {
            System.out.print("Category name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Category description: ");
            String description = scanner.nextLine().trim();

            try {
                ValidationService.validateCategoryName(name);
                ValidationService.validateCategoryDescription(description);

                return new Category(name, description, type);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
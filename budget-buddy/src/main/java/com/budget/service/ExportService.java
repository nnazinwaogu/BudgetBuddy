package com.budget.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.budget.model.Transaction;
import com.budget.repository.TransactionRepository;
import com.budget.util.FileUtil;

public final class ExportService {

    private final TransactionRepository transactionRepository;

    public ExportService(TransactionRepository transactionRepository) {
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "TransactionRepository cannot be null");
    }

    public void exportToCsv(List<Transaction> transactions, String filePath) throws IOException {
        Objects.requireNonNull(transactions, "Transactions cannot be null");
        Objects.requireNonNull(filePath, "File path cannot be null");

        List<String> lines = new ArrayList<>();
        lines.add("ID,Description,Amount,Date,Category,Type,Notes");

        for (Transaction t : transactions) {
            lines.add(toCsvRow(t));
        }

        FileUtil.writeLines(filePath, lines);
    }

    public void exportAllToCsv(String filePath) throws IOException {
        exportToCsv(transactionRepository.findAll(), filePath);
    }

    private String toCsvRow(Transaction t) {
        return String.join(",",
            escapeCsv(t.getId()),
            escapeCsv(t.getDescription()),
            escapeCsv(t.getAmount().toString()),
            escapeCsv(t.getDate().toString()),
            escapeCsv(t.getCategory().getName()),
            escapeCsv(t.getCategory().getTransactionType().name()),
            escapeCsv(t.getNotes() != null ? t.getNotes() : "")
        );
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
package com.budget.util;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.budget.model.Transaction;

public final class CsvExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String[] HEADERS = {"Date", "Type", "Category", "Amount", "Description"};
    private static final String CSV_LINE_SEPARATOR = System.lineSeparator();

    private CsvExporter() {
        // utility class
    }

    public static void exportTransactions(List<Transaction> transactions, String filePath) throws IOException {
        Objects.requireNonNull(transactions, "Transactions list cannot be null");
        Objects.requireNonNull(filePath, "File path cannot be null");

        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        try (Writer writer = Files.newBufferedWriter(path)) {
            exportTransactionsToWriter(transactions, writer);
        }
    }

    public static void exportTransactionsToWriter(List<Transaction> transactions, Writer writer) {
        Objects.requireNonNull(transactions, "Transactions list cannot be null");
        Objects.requireNonNull(writer, "Writer cannot be null");

        writeLine(writer, HEADERS);

        for (Transaction transaction : transactions) {
            writeDataRow(writer, transaction);
        }
    }

    private static void writeDataRow(Writer writer, Transaction transaction) {
        String date = DATE_FORMATTER.format(transaction.getDate());
        String type = transaction.getCategory().getTransactionType().name();
        String category = transaction.getCategory().getName();
        String amount = transaction.getAmount().setScale(2, RoundingMode.HALF_UP).toString();
        String description = escapeCsvField(transaction.getDescription());

        writeLine(writer, date, type, category, amount, description);
    }

    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            String escaped = field.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        }
        return field;
    }

    private static void writeLine(Writer writer, String... columns) {
        String line = String.join(",", columns) + CSV_LINE_SEPARATOR;
        try {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV output", e);
        }
    }
}
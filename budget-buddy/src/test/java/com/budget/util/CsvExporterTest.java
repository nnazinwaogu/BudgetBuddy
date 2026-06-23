package com.budget.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;

class CsvExporterTest {

    @Test
    void testHeaderRow() {
        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(new ArrayList<>(), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());

        assertEquals(1, lines.length, "Should only have header row for empty list");
        assertEquals("Date,Type,Category,Amount,Description", lines[0]);
    }

    @Test
    void testSingleTransaction() {
        Category category = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        Transaction transaction = new Transaction(
            "Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), category, null
        );

        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(List.of(transaction), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());

        assertEquals(2, lines.length, "Should have header + 1 data row");
        assertEquals("Date,Type,Category,Amount,Description", lines[0]);

        String[] columns = lines[1].split(",");
        assertEquals("2024-01-15", columns[0]);
        assertEquals("EXPENSE", columns[1]);
        assertEquals("Food", columns[2]);
        assertEquals("15.50", columns[3]);
        assertEquals("Lunch", columns[4]);
    }

    @Test
    void testMultipleTransactions() {
        Category food = new Category("Food", "Food expenses", TransactionType.EXPENSE);
        Category salary = new Category("Salary", "Monthly salary", TransactionType.INCOME);

        Transaction t1 = new Transaction(
            "Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), food, null
        );
        Transaction t2 = new Transaction(
            "Salary Jan", new BigDecimal("5000.00"), LocalDate.of(2024, 1, 1), salary, null
        );

        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(List.of(t1, t2), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());

        assertEquals(3, lines.length, "Should have header + 2 data rows");

        // t1 data row
        assertTrue(lines[1].contains("2024-01-15"));
        assertTrue(lines[1].contains("EXPENSE"));
        assertTrue(lines[1].contains("Food"));
        assertTrue(lines[1].contains("15.50"));
        assertTrue(lines[1].contains("Lunch"));

        // t2 data row
        assertTrue(lines[2].contains("2024-01-01"));
        assertTrue(lines[2].contains("INCOME"));
        assertTrue(lines[2].contains("Salary"));
        assertTrue(lines[2].contains("5000.00"));
        assertTrue(lines[2].contains("Salary Jan"));
    }

    @Test
    void testEmptyTransactionList() {
        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(new ArrayList<>(), writer);

        String output = writer.toString().trim();
        assertEquals("Date,Type,Category,Amount,Description", output,
            "Empty list should produce only header row");
    }

    @Test
    void testDescriptionWithComma() {
        Category category = new Category("Misc", "Misc", TransactionType.EXPENSE);
        Transaction transaction = new Transaction(
            "Lunch, dinner, and snacks", new BigDecimal("30.00"),
            LocalDate.of(2024, 2, 1), category, null
        );

        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(List.of(transaction), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());
        String dataRow = lines[1];

        // Description with commas should be quoted
        assertTrue(dataRow.contains("\"Lunch, dinner, and snacks\""),
            "Description with commas should be quoted");
    }

    @Test
    void testDescriptionWithQuotes() {
        Category category = new Category("Misc", "Misc", TransactionType.EXPENSE);
        Transaction transaction = new Transaction(
            "Lunch \"special\" deal", new BigDecimal("12.00"),
            LocalDate.of(2024, 3, 1), category, null
        );

        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(List.of(transaction), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());
        String dataRow = lines[1];

        // Description with quotes should have them escaped (doubled) and field quoted
        assertTrue(dataRow.contains("\"Lunch \"\"special\"\" deal\""),
            "Description with quotes should be escaped and quoted");
    }

    @Test
    void testAmountFormatting() {
        Category category = new Category("Food", "Food", TransactionType.EXPENSE);
        Transaction transaction = new Transaction(
            "Test", new BigDecimal("100.00"), LocalDate.of(2024, 4, 1), category, null
        );

        StringWriter writer = new StringWriter();
        CsvExporter.exportTransactionsToWriter(List.of(transaction), writer);

        String output = writer.toString().trim();
        String[] lines = output.split(System.lineSeparator());
        String dataRow = lines[1];

        // Amount should be the decimal string from BigDecimal (stripTrailingZeros friendly)
        String amountColumn = dataRow.split(",")[3];
        assertTrue(amountColumn.equals("100.00") || amountColumn.equals("100"),
            "Amount should be formatted correctly");
    }

    @Test
    void testExportTransactionsToFile() throws Exception {
        Category category = new Category("Food", "Food", TransactionType.EXPENSE);
        Transaction transaction = new Transaction(
            "Lunch", new BigDecimal("15.50"), LocalDate.of(2024, 1, 15), category, null
        );

        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("csv-test-", ".csv");
        try {
            CsvExporter.exportTransactions(List.of(transaction), tempFile.toString());

            String content = new String(java.nio.file.Files.readAllBytes(tempFile));
            String[] lines = content.trim().split(System.lineSeparator());

            assertEquals(2, lines.length);
            assertEquals("Date,Type,Category,Amount,Description", lines[0]);
            assertTrue(lines[1].contains("2024-01-15"));
            assertTrue(lines[1].contains("Lunch"));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testNullTransactionList() {
        StringWriter writer = new StringWriter();
        assertThrows(NullPointerException.class, () ->
            CsvExporter.exportTransactionsToWriter(null, writer));
    }

    @Test
    void testNullWriter() {
        assertThrows(NullPointerException.class, () ->
            CsvExporter.exportTransactionsToWriter(new ArrayList<>(), null));
    }
}
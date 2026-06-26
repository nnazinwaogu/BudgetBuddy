package com.budget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;
import com.budget.repository.InMemoryTransactionRepository;
import com.budget.repository.TransactionRepository;
import com.budget.util.FileUtil;

class ExportServiceTest {

    private ExportService exportService;
    private TransactionRepository transactionRepository;

    private Category foodCategory;
    private Category salaryCategory;

    @BeforeEach
    void setUp() {
        transactionRepository = new InMemoryTransactionRepository();
        exportService = new ExportService(transactionRepository);

        foodCategory = new Category("Food", "Groceries and dining", TransactionType.EXPENSE);
        salaryCategory = new Category("Salary", "Monthly income", TransactionType.INCOME);
    }

    @Test
    void testValidExportToCsvCreatesFileWithHeaderAndData() throws IOException {
        Transaction t = new Transaction("Grocery shopping", new BigDecimal("45.99"),
            LocalDate.of(2024, 1, 15), foodCategory, "Weekly groceries");

        String filePath = "target/test-export-basic.csv";
        exportService.exportToCsv(List.of(t), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        assertEquals(2, lines.size());
        assertEquals("ID,Description,Amount,Date,Category,Type,Notes", lines.get(0));

        String[] fields = lines.get(1).split(",", 7);
        assertEquals(t.getId(), fields[0]);
        assertEquals("Grocery shopping", fields[1]);
        assertEquals("45.99", fields[2]);
        assertEquals("2024-01-15", fields[3]);
        assertEquals("Food", fields[4]);
        assertEquals("EXPENSE", fields[5]);
        assertEquals("Weekly groceries", fields[6]);

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvEmptyList() throws IOException {
        String filePath = "target/test-export-empty.csv";
        exportService.exportToCsv(new ArrayList<>(), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        assertEquals(1, lines.size());
        assertEquals("ID,Description,Amount,Date,Category,Type,Notes", lines.get(0));

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvMultipleTransactions() throws IOException {
        Transaction t1 = new Transaction("Lunch", new BigDecimal("15.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null);
        Transaction t2 = new Transaction("Salary deposit", new BigDecimal("3000.00"),
            LocalDate.of(2024, 1, 1), salaryCategory, "January salary");

        String filePath = "target/test-export-multiple.csv";
        exportService.exportToCsv(Arrays.asList(t1, t2), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        assertEquals(3, lines.size());
        assertEquals("ID,Description,Amount,Date,Category,Type,Notes", lines.get(0));
        assertTrue(lines.get(1).contains(t1.getId()));
        assertTrue(lines.get(2).contains(t2.getId()));

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvEscapesCommaInDescription() throws IOException {
        Transaction t = new Transaction("Shopping, groceries, and supplies", new BigDecimal("78.50"),
            LocalDate.of(2024, 2, 10), foodCategory, null);

        String filePath = "target/test-export-comma.csv";
        exportService.exportToCsv(List.of(t), filePath);

        String content = FileUtil.readString(filePath);
        assertTrue(content.contains("\"Shopping, groceries, and supplies\""),
            "Description with comma should be quoted in CSV");

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvEscapesQuoteInNotes() throws IOException {
        Transaction t = new Transaction("Dinner", new BigDecimal("32.00"),
            LocalDate.of(2024, 3, 5), foodCategory, "Said \"thank you\"");

        String filePath = "target/test-export-quote.csv";
        exportService.exportToCsv(List.of(t), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        String dataLine = lines.get(1);
        assertTrue(dataLine.contains("\"Said \"\"thank you\"\"\""));

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvNullNotes() throws IOException {
        Transaction t = new Transaction("Coffee", new BigDecimal("4.50"),
            LocalDate.of(2024, 4, 1), foodCategory, null);

        String filePath = "target/test-export-nullnotes.csv";
        exportService.exportToCsv(List.of(t), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        String[] fields = lines.get(1).split(",", 7);
        assertEquals("", fields[6]);

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportAllToCsvExportsAllFromRepository() throws IOException {
        transactionRepository.save(new Transaction("Lunch", new BigDecimal("15.00"),
            LocalDate.of(2024, 1, 15), foodCategory, null));
        transactionRepository.save(new Transaction("Salary", new BigDecimal("3000.00"),
            LocalDate.of(2024, 1, 1), salaryCategory, "January"));

        String filePath = "target/test-export-all.csv";
        exportService.exportAllToCsv(filePath);

        List<String> lines = FileUtil.readLines(filePath);
        assertEquals(3, lines.size());

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testExportToCsvNullTransactionsValidation() {
        assertThrows(NullPointerException.class,
            () -> exportService.exportToCsv(null, "target/test.csv"));
    }

    @Test
    void testExportToCsvNullFilePathValidation() throws IOException {
        Transaction t = new Transaction("Test", BigDecimal.ONE,
            LocalDate.of(2024, 1, 1), foodCategory, null);
        assertThrows(NullPointerException.class,
            () -> exportService.exportToCsv(List.of(t), null));
    }

    @Test
    void testValidExportToCsvIncomeAndExpenseMixed() throws IOException {
        Transaction income = new Transaction("Freelance payment", new BigDecimal("1500.00"),
            LocalDate.of(2024, 5, 1), salaryCategory, "May freelance");
        Transaction expense = new Transaction("Restaurant", new BigDecimal("65.00"),
            LocalDate.of(2024, 5, 10), foodCategory, null);

        String filePath = "target/test-export-mixed.csv";
        exportService.exportToCsv(Arrays.asList(income, expense), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        assertEquals(3, lines.size());
        assertTrue(lines.get(1).endsWith("May freelance"));
        assertTrue(lines.get(2).endsWith(""));

        FileUtil.deleteFile(filePath);
    }

    @Test
    void testValidExportToCsvAmountFormatting() throws IOException {
        Transaction t = new Transaction("Precise amount", new BigDecimal("99.00"),
            LocalDate.of(2024, 6, 15), foodCategory, null);

        String filePath = "target/test-export-decimals.csv";
        exportService.exportToCsv(List.of(t), filePath);

        List<String> lines = FileUtil.readLines(filePath);
        String[] fields = lines.get(1).split(",", 7);
        assertEquals("99.00", fields[2]);

        FileUtil.deleteFile(filePath);
    }
}
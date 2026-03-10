package com.budget.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for common file operations.
 * Provides methods for reading, writing, and managing files and directories.
 */
public final class FileUtil {
    
    private FileUtil() {
        // Prevent instantiation
    }
    
    /**
     * Ensures that the directory for the given file path exists.
     * Creates the directory if it doesn't exist.
     *
     * @param filePath the path to the file
     * @throws IOException if an I/O error occurs
     */
    public static void ensureDirectoryExists(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
    
    /**
     * Ensures that the file exists. Creates an empty file if it doesn't exist.
     *
     * @param filePath the path to the file
     * @throws IOException if an I/O error occurs
     */
    public static void ensureFileExists(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        ensureDirectoryExists(filePath);
        
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
    
    /**
     * Checks if a file exists.
     *
     * @param filePath the path to the file
     * @return true if the file exists, false otherwise
     */
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }
    
    /**
     * Reads all lines from a file.
     *
     * @param filePath the path to the file
     * @return a list of all lines in the file
     * @throws IOException if an I/O error occurs
     */
    public static List<String> readLines(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines().collect(Collectors.toList());
        }
    }
    
    /**
     * Writes a list of lines to a file. Overwrites existing content.
     *
     * @param filePath the path to the file
     * @param lines the lines to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        if (lines == null) {
            throw new IllegalArgumentException("Lines cannot be null");
        }
        
        ensureDirectoryExists(filePath);
        
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Appends a line to a file.
     *
     * @param filePath the path to the file
     * @param line the line to append
     * @throws IOException if an I/O error occurs
     */
    public static void appendLine(String filePath, String line) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        if (line == null) {
            throw new IllegalArgumentException("Line cannot be null");
        }
        
        ensureDirectoryExists(filePath);
        
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, 
                java.nio.file.StandardOpenOption.CREATE, 
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        }
    }
    
    /**
     * Deletes a file.
     *
     * @param filePath the path to the file
     * @return true if the file was deleted, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean deleteFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return Files.deleteIfExists(path);
        }
        return false;
    }
    
    /**
     * Gets the absolute path of a file.
     *
     * @param filePath the path to the file
     * @return the absolute path as a string
     */
    public static String getAbsolutePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        return Paths.get(filePath).toAbsolutePath().toString();
    }
    
    /**
     * Reads the entire content of a file as a single string.
     *
     * @param filePath the path to the file
     * @return the file content as a string
     * @throws IOException if an I/O error occurs
     */
    public static String readString(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
    
    /**
     * Writes a string to a file. Overwrites existing content.
     *
     * @param filePath the path to the file
     * @param content the content to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeString(String filePath, String content) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        
        ensureDirectoryExists(filePath);
        
        Path path = Paths.get(filePath);
        Files.writeString(path, content);
    }
}

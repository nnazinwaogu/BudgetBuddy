package com.budget.util;

import com.budget.model.Budget;
import com.budget.model.Category;
import com.budget.model.Transaction;
import com.budget.model.TransactionType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for JSON serialization/deserialization using Jackson.
 * Handles custom serialization for LocalDate, LocalDateTime, and domain model classes.
 */
public final class JacksonJsonUtil {
    
    private static final ObjectMapper mapper = createObjectMapper();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    private JacksonJsonUtil() {
        // Prevent instantiation
    }
    
    /**
     * Creates and configures the ObjectMapper with custom modules.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register Java Time module for LocalDate/LocalDateTime
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // Add LocalDate serializer
        javaTimeModule.addSerializer(LocalDate.class, new com.fasterxml.jackson.databind.JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(DATE_FORMATTER));
            }
        });
        
        // Add LocalDate deserializer
        javaTimeModule.addDeserializer(LocalDate.class, new com.fasterxml.jackson.databind.JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                return LocalDate.parse(p.getText(), DATE_FORMATTER);
            }
        });
        
        // Add LocalDateTime serializer
        javaTimeModule.addSerializer(LocalDateTime.class, new com.fasterxml.jackson.databind.JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(DATE_TIME_FORMATTER));
            }
        });
        
        // Add LocalDateTime deserializer
        javaTimeModule.addDeserializer(LocalDateTime.class, new com.fasterxml.jackson.databind.JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getText(), DATE_TIME_FORMATTER);
            }
        });
        
        objectMapper.registerModule(javaTimeModule);
        
        // Configure to fail on unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return objectMapper;
    }
    
    /**
     * Serializes an object to a JSON string.
     *
     * @param object the object to serialize
     * @return the JSON string representation
     * @throws IOException if serialization fails
     */
    public static String serialize(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    /**
     * Deserializes a JSON string to an object of the specified class.
     *
     * @param json the JSON string
     * @param clazz the target class
     * @param <T> the type to deserialize to
     * @return the deserialized object
     * @throws IOException if deserialization fails
     */
    public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }
    
    /**
     * Deserializes a JSON string to a List of objects.
     *
     * @param json the JSON string (should be an array)
     * @param elementClass the class of list elements
     * @param <T> the element type
     * @return a List of deserialized objects
     * @throws IOException if deserialization fails
     */
    public static <T> List<T> deserializeList(String json, Class<T> elementClass) throws IOException {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
    }
    
    /**
     * Serializes a List of objects to a JSON array string.
     *
     * @param list the list to serialize
     * @param <T> the element type
     * @return the JSON array string
     * @throws IOException if serialization fails
     */
    public static <T> String serializeList(List<T> list) throws IOException {
        if (list == null) {
            return "[]";
        }
        return mapper.writeValueAsString(list);
    }
    
    /**
     * Writes an object as JSON to a file.
     *
     * @param filePath the file path
     * @param object the object to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeObjectToFile(String filePath, Object object) throws IOException {
        String json = serialize(object);
        FileUtil.writeString(filePath, json);
    }
    
    /**
     * Reads an object from a JSON file.
     *
     * @param filePath the file path
     * @param clazz the target class
     * @param <T> the type to read
     * @return the deserialized object
     * @throws IOException if an I/O error occurs
     */
    public static <T> T readObjectFromFile(String filePath, Class<T> clazz) throws IOException {
        String json = FileUtil.readString(filePath);
        return deserialize(json, clazz);
    }
    
    /**
     * Writes a list of objects as JSON array to a file.
     *
     * @param filePath the file path
     * @param list the list to write
     * @param <T> the element type
     * @throws IOException if an I/O error occurs
     */
    public static <T> void writeListToFile(String filePath, List<T> list) throws IOException {
        String json = serializeList(list);
        FileUtil.writeString(filePath, json);
    }
    
    /**
     * Reads a list of objects from a JSON file.
     *
     * @param filePath the file path
     * @param elementClass the class of list elements
     * @param <T> the element type
     * @return a List of deserialized objects
     * @throws IOException if an I/O error occurs
     */
    public static <T> List<T> readListFromFile(String filePath, Class<T> elementClass) throws IOException {
        String json = FileUtil.readString(filePath);
        return deserializeList(json, elementClass);
    }
    
    /**
     * Serializes a Transaction to a JSON string.
     *
     * @param transaction the transaction to serialize
     * @return the JSON string
     * @throws IOException if serialization fails
     */
    public static String serializeTransaction(Transaction transaction) throws IOException {
        return serialize(transaction);
    }
    
    /**
     * Deserializes a Transaction from a JSON string.
     *
     * @param json the JSON string
     * @return the Transaction object
     * @throws IOException if deserialization fails
     */
    public static Transaction deserializeTransaction(String json) throws IOException {
        return deserialize(json, Transaction.class);
    }
    
    /**
     * Serializes a Category to a JSON string.
     *
     * @param category the category to serialize
     * @return the JSON string
     * @throws IOException if serialization fails
     */
    public static String serializeCategory(Category category) throws IOException {
        return serialize(category);
    }
    
    /**
     * Deserializes a Category from a JSON string.
     *
     * @param json the JSON string
     * @return the Category object
     * @throws IOException if deserialization fails
     */
    public static Category deserializeCategory(String json) throws IOException {
        return deserialize(json, Category.class);
    }
    
    /**
     * Serializes a Budget to a JSON string.
     *
     * @param budget the budget to serialize
     * @return the JSON string
     * @throws IOException if serialization fails
     */
    public static String serializeBudget(Budget budget) throws IOException {
        return serialize(budget);
    }
    
    /**
     * Deserializes a Budget from a JSON string.
     *
     * @param json the JSON string
     * @return the Budget object
     * @throws IOException if deserialization fails
     */
    public static Budget deserializeBudget(String json) throws IOException {
        return deserialize(json, Budget.class);
    }
}

package com.kwedinger.blog.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(FORMATTER);
    }
    
    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        
        // Try parsing as ISO format first
        try {
            return LocalDateTime.parse(dbData, FORMATTER);
        } catch (DateTimeParseException e) {
            // If that fails, try parsing as Unix timestamp (milliseconds)
            try {
                long timestamp = Long.parseLong(dbData);
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            } catch (NumberFormatException nfe) {
                // If both fail, try ISO format with space separator (common SQLite format)
                try {
                    return LocalDateTime.parse(dbData.replace(' ', 'T'));
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Unable to parse date: " + dbData, ex);
                }
            }
        }
    }
}

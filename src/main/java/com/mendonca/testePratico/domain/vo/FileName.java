package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;

public record FileName(String value) {
    
    private static final int MAX_LENGTH = 255;
    
    public FileName {
        if (value == null || value.isBlank()) {
            throw new ProcessingException("Filename cannot be empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new ProcessingException("Filename too long (max " + MAX_LENGTH + " characters)");
        }
    }
    
    public static FileName of(String value) {
        return new FileName(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
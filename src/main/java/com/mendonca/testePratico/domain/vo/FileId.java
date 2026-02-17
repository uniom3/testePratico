package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import java.util.UUID;

public record FileId(String value) {
    
    public FileId {
        if (value == null || value.isBlank()) {
            throw new ProcessingException("File ID cannot be empty");
        }
    }
    
    public static FileId of(String value) {
        return new FileId(value);
    }
    
    public static FileId generate() {
        return new FileId(UUID.randomUUID().toString());
    }
    
    @Override
    public String toString() {
        return value;
    }
}
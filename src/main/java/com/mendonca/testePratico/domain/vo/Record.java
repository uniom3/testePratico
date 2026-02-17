package com.mendonca.testePratico.domain.vo;

public record Record(String type, double value, boolean valid) {
    
    public static Record valid(String type, double value) {
        return new Record(type, value, true);
    }
    
    public static Record invalid(String line) {
        return new Record("INVALID", 0, false);
    }
}
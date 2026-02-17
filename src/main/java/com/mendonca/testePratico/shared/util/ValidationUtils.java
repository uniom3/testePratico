package com.mendonca.testePratico.shared.util;

public final class ValidationUtils {
    
    private ValidationUtils() {}
    
    public static boolean isValidHeader(String line, String... validPatterns) {
        if (line == null) return false;
        
        for (String pattern : validPatterns) {
            if (line.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isExactMatch(String line, String expected) {
        return line != null && line.trim().equals(expected);
    }
}
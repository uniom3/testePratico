package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;

public record Progress(int percentage) {
    
    public Progress {
        if (percentage < 0 || percentage > 100) {
            throw new ProcessingException("Progress must be between 0 and 100");
        }
    }
    
    public static Progress initial() {
        return new Progress(0);
    }
    
    public static Progress complete() {
        return new Progress(100);
    }
    
    public static Progress of(int percentage) {
        return new Progress(percentage);
    }
}
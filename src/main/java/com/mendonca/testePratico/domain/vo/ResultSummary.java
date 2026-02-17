package com.mendonca.testePratico.domain.vo;

import java.util.Map;

public record ResultSummary(Map<String, Object> summary) {
    
    public static ResultSummary of(Summary summary) {
        return new ResultSummary(summary.toMap());
    }
}
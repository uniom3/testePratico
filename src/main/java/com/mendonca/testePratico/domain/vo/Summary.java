package com.mendonca.testePratico.domain.vo;

import java.util.HashMap;
import java.util.Map;

public class Summary {
    private final Map<String, Long> recordsByType;
    private final Map<String, Double> totalsByType;
    private final long totalRecords;
    
    private Summary(Builder builder) {
        this.recordsByType = new HashMap<>(builder.recordsByType);
        this.totalsByType = new HashMap<>(builder.totalsByType);
        this.totalRecords = builder.totalRecords;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("recordsByType", recordsByType);
        map.put("totalsByType", totalsByType);
        map.put("totalRecords", totalRecords);
        map.put("processedAt", java.time.LocalDateTime.now().toString());
        return map;
    }
    
    public static class Builder {
        private final Map<String, Long> recordsByType = new HashMap<>();
        private final Map<String, Double> totalsByType = new HashMap<>();
        private long totalRecords = 0;
        
        public Builder addRecord(String type, double value) {
            recordsByType.merge(type, 1L, Long::sum);
            totalsByType.merge(type, value, Double::sum);
            totalRecords++;
            return this;
        }
        
        public Summary build() {
            return new Summary(this);
        }
    }
}
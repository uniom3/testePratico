package com.mendonca.testePratico.domain.service;

import com.mendonca.testePratico.domain.vo.Record;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecordParser {
    
    public Record parse(String line) {
        if (line == null || line.isBlank()) {
            return Record.invalid(line);
        }
        
        String[] parts = line.split("\\|", -1);
        
        if (parts.length < 2) {
            return Record.invalid(line);
        }
        
        String type = parts[1];
        double value = 0.0;
        
        if (parts.length > 3 && !parts[3].isEmpty()) {
            try {
                value = Double.parseDouble(parts[3].replace(",", "."));
            } catch (NumberFormatException e) {
                log.debug("Non-numeric value found in field 3: {}", parts[3]);
            }
        }
        
        return Record.valid(type, value);
    }
}
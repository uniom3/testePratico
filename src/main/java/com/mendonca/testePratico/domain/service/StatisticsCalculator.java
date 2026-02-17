package com.mendonca.testePratico.domain.service;

import com.mendonca.testePratico.domain.vo.Record;
import com.mendonca.testePratico.domain.vo.Summary;

import java.util.stream.Stream;

public class StatisticsCalculator {
    
    public Summary calculate(Stream<Record> records) {
        Summary.Builder builder = new Summary.Builder();
        
        records.filter(Record::valid)
               .forEach(record -> builder.addRecord(record.type(), record.value()));
        
        return builder.build();
    }
}
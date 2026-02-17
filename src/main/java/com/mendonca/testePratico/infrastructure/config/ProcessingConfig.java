package com.mendonca.testePratico.infrastructure.config;

import com.mendonca.testePratico.domain.service.FileValidator;
import com.mendonca.testePratico.domain.service.RecordParser;
import com.mendonca.testePratico.domain.service.StatisticsCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessingConfig {
    
    @Bean
    public FileValidator fileValidator() {
        return new FileValidator();
    }
    
    @Bean
    public RecordParser recordParser() {
        return new RecordParser();
    }
    
    @Bean
    public StatisticsCalculator statisticsCalculator() {
        return new StatisticsCalculator();
    }
}
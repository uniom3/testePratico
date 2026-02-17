package com.mendonca.testePratico.infrastructure.adapter.output.persistence;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.vo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileProcessingMapper {
    
    private final ObjectMapper objectMapper;
    
    public FileProcessingEntity toEntity(FileProcessing domain) {
        FileProcessingEntity entity = new FileProcessingEntity();
        entity.setId(domain.getId().value());
        entity.setFilename(domain.getFilename().value());
        entity.setStatus(domain.getStatus().name());
        entity.setProgress(domain.getProgress().percentage());
        entity.setErrorMessage(domain.getErrorMessage());
        entity.setCreatedAt(domain.getAuditInfo().getCreatedAt());
        entity.setUpdatedAt(domain.getAuditInfo().getUpdatedAt());
        entity.setStartedAt(domain.getAuditInfo().getStartedAt());
        entity.setCompletedAt(domain.getAuditInfo().getCompletedAt());
        
        if (domain.getResultSummary() != null) {
            try {
                entity.setResultSummary(
                    objectMapper.writeValueAsString(domain.getResultSummary().summary())
                );
            } catch (Exception e) {
                log.error("Error serializing result summary", e);
            }
        }
        
        return entity;
    }
    
    public FileProcessing toDomain(FileProcessingEntity entity) {
        FileProcessing fileProcessing = FileProcessing.create(
            entity.getId(),
            entity.getFilename()
        );
        
        switch (entity.getStatus()) {
            case "PROCESSING" -> fileProcessing.start();
            case "COMPLETED" -> {
                fileProcessing.start();
                fileProcessing.complete(parseSummary(entity.getResultSummary()));
            }
            case "ERROR" -> fileProcessing.fail(entity.getErrorMessage());
        }
        
        return fileProcessing;
    }
    
    private Summary parseSummary(String json) {
        if (json == null) return new Summary.Builder().build();
        
        try {
            Summary.Builder builder = new Summary.Builder();
            
            return builder.build();
        } catch (Exception e) {
            log.error("Error parsing result summary", e);
            return new Summary.Builder().build();
        }
    }
}
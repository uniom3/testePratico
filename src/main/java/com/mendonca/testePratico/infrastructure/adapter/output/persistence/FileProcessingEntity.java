package com.mendonca.testePratico.infrastructure.adapter.output.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_processing")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileProcessingEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String filename;
    
    @Column(nullable = false)
    private String status;
    
    private int progress;
    
    @Column(columnDefinition = "TEXT")
    private String resultSummary;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
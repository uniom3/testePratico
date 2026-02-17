package com.mendonca.testePratico.domain.vo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuditInfo {
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    private AuditInfo() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    public static AuditInfo create() {
        return new AuditInfo();
    }
    
    public void started() {
        this.startedAt = LocalDateTime.now();
        this.updatedAt = this.startedAt;
    }
    
    public void updated() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completed() {
        this.completedAt = LocalDateTime.now();
        this.updatedAt = this.completedAt;
    }
    
    public void failed() {
        this.updatedAt = LocalDateTime.now();
    }
}
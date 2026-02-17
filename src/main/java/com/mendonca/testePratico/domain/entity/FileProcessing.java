package com.mendonca.testePratico.domain.entity;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import com.mendonca.testePratico.domain.vo.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FileProcessing {
    private final FileId id;
    private final FileName filename;
    private ProcessingStatus status;
    private Progress progress;
    private ResultSummary resultSummary;
    private String errorMessage;
    private final AuditInfo auditInfo;
    
    private FileProcessing(FileId id, FileName filename) {
        this.id = id;
        this.filename = filename;
        this.status = ProcessingStatus.PENDING;
        this.progress = Progress.initial();
        this.auditInfo = AuditInfo.create();
    }
    
    public static FileProcessing create(String id, String filename) {
        return new FileProcessing(
            FileId.of(id),
            FileName.of(filename)
        );
    }
    
    public void start() {
        validateState();
        this.status = ProcessingStatus.PROCESSING;
        this.progress = Progress.of(0);
        this.auditInfo.started();
    }
    
    public void updateProgress(int percentage) {
        if (this.status != ProcessingStatus.PROCESSING) {
            throw new ProcessingException(
                String.format("Cannot update progress when status is %s", this.status)
            );
        }
        this.progress = Progress.of(percentage);
        this.auditInfo.updated();
    }
    
    public void complete(Summary summary) {
        if (this.status != ProcessingStatus.PROCESSING) {
            throw new ProcessingException(
                String.format("Cannot complete when status is %s", this.status)
            );
        }
        this.status = ProcessingStatus.COMPLETED;
        this.progress = Progress.complete();
        this.resultSummary = ResultSummary.of(summary);
        this.auditInfo.completed();
    }
    
    public void fail(String error) {
        this.status = ProcessingStatus.ERROR;
        this.errorMessage = error;
        this.auditInfo.failed();
    }
    
    public boolean isComplete() {
        return this.status == ProcessingStatus.COMPLETED;
    }
    
    public boolean hasError() {
        return this.status == ProcessingStatus.ERROR;
    }
    
    private void validateState() {
        if (this.id == null || this.filename == null) {
            throw new ProcessingException("Invalid file processing state");
        }
    }
    
    public Map<String, Object> toSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("id", id.value());
        summary.put("filename", filename.value());
        summary.put("status", status.name());
        summary.put("progress", progress.percentage());
        summary.put("createdAt", auditInfo.getCreatedAt());
        summary.put("updatedAt", auditInfo.getUpdatedAt());
        
        if (resultSummary != null) {
            summary.put("result", resultSummary.summary());
        }
        
        if (errorMessage != null) {
            summary.put("error", errorMessage);
        }
        
        return summary;
    }
}
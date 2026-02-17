package com.mendonca.testePratico.infrastructure.event;

import com.mendonca.testePratico.infrastructure.metrics.ProcessingMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileEventListener {
    
    private final ProcessingMetrics metrics;
    
    @EventListener
    @Async
    public void handleFileUploaded(FileUploadedEvent event) {
        log.info("File uploaded: {} (ID: {}, Size: {} bytes)", 
                event.getFilename(), event.getFileId(), event.getFileSize());
        metrics.recordUpload();
    }
    
    @EventListener
    @Async
    public void handleFileProcessed(FileProcessedEvent event) {
        if (event.isSuccess()) {
            log.info("File processed successfully: {} in {} ms", 
                    event.getFileId(), event.getProcessingTime().toMillis());
            metrics.recordSuccess();
        } else {
            log.error("File processing failed: {}", event.getFileId());
            metrics.recordError();
        }
    }
}
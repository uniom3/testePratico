package com.mendonca.testePratico.infrastructure.adapter.output.processor;

import com.mendonca.testePratico.application.port.output.FileProcessingPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("batch")
@Slf4j
public class BatchFileProcessor implements FileProcessingPort {
    
    @Override
    public void processAsync(MultipartFile file, String fileId) {
        log.info("Processing file {} in batch mode", fileId);
    }
    
    @Override
    public void cancel(String fileId) {
        log.info("Cancelling batch processing for file: {}", fileId);
    }
}
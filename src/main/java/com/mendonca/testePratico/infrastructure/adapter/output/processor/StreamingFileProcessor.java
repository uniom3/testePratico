package com.mendonca.testePratico.infrastructure.adapter.output.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mendonca.testePratico.application.port.output.FileProcessingPort;
import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.repository.FileProcessingRepository;
import com.mendonca.testePratico.domain.service.RecordParser;
import com.mendonca.testePratico.domain.vo.FileId;
import com.mendonca.testePratico.domain.vo.Summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreamingFileProcessor implements FileProcessingPort {
    
    private final FileProcessingRepository repository;
    private final RecordParser recordParser;
    
    @Override
    public void processAsync(MultipartFile file, String fileId) {
        log.info("Starting async processing for file: {}", fileId);
        
        try {
            FileProcessing processing = repository.findById(FileId.of(fileId))
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
            
            long totalLines = countLines(file);
            log.info("Total lines to process: {}", totalLines);
            
            Summary.Builder summaryBuilder = new Summary.Builder();
            AtomicLong processedLines = new AtomicLong(0);
            
            try (LineIterator iterator = IOUtils.lineIterator(
                    file.getInputStream(), StandardCharsets.UTF_8)) {
                
                while (iterator.hasNext()) {
                    String line = iterator.nextLine();
                    if (!line.isBlank()) {
                        var record = recordParser.parse(line);
                        if (record.valid()) {
                            summaryBuilder.addRecord(record.type(), record.value());
                        }
                    }
                    
                    long processed = processedLines.incrementAndGet();
                    if (processed % 1000 == 0) {
                        int progress = (int) ((processed * 100) / totalLines);
                        processing.updateProgress(progress);
                        repository.save(processing);
                        log.debug("Progress for {}: {}%", fileId, progress);
                    }
                }
            }
            
            Summary summary = summaryBuilder.build();
            processing.complete(summary);
            repository.save(processing);
            
            log.info("Completed processing for file: {}", fileId);
            
        } catch (Exception e) {
            log.error("Error processing file: " + fileId, e);
            handleError(fileId, e);
        }
    }
    
    @Override
    public void cancel(String fileId) {
        log.info("Cancelling processing for file: {}", fileId);
    }
    
    private long countLines(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
            return reader.lines().count();
        } catch (Exception e) {
            log.error("Error counting lines", e);
            return 0;
        }
    }
    
    private void handleError(String fileId, Exception e) {
        repository.findById(FileId.of(fileId)).ifPresent(processing -> {
            processing.fail(e.getMessage());
            repository.save(processing);
        });
    }
}
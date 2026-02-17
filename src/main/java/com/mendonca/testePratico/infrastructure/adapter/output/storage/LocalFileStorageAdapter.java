package com.mendonca.testePratico.infrastructure.adapter.output.storage;

import com.mendonca.testePratico.application.port.output.FileStoragePort;
import com.mendonca.testePratico.domain.exception.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class LocalFileStorageAdapter implements FileStoragePort {
    
    private final Path storagePath;
    
    public LocalFileStorageAdapter(
            @Value("${app.processing.temp-directory}") String tempDir) {
        this.storagePath = Paths.get(tempDir);
        createDirectoryIfNotExists();
    }
    
    private void createDirectoryIfNotExists() {
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
                log.info("Created storage directory: {}", storagePath);
            }
        } catch (IOException e) {
            throw new ProcessingException("Failed to create storage directory", e);
        }
    }
    
    @Override
    public void store(MultipartFile file, String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            Files.copy(file.getInputStream(), filePath);
            log.info("File stored: {}", filePath);
        } catch (IOException e) {
            throw new ProcessingException("Failed to store file: " + fileId, e);
        }
    }
    
    @Override
    public InputStream retrieve(String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new ProcessingException("Failed to retrieve file: " + fileId, e);
        }
    }
    
    @Override
    public void delete(String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            Files.deleteIfExists(filePath);
            log.info("File deleted: {}", filePath);
        } catch (IOException e) {
            throw new ProcessingException("Failed to delete file: " + fileId, e);
        }
    }
    
    @Override
    public boolean exists(String fileId) {
        Path filePath = storagePath.resolve(fileId);
        return Files.exists(filePath);
    }
}
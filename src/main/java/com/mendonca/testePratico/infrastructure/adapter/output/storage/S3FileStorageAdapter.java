package com.mendonca.testePratico.infrastructure.adapter.output.storage;

import com.mendonca.testePratico.application.port.output.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
@Profile("prod")
@Slf4j
public class S3FileStorageAdapter implements FileStoragePort {
    
    
    // private final AmazonS3 s3Client;
    
    public S3FileStorageAdapter() {
    }
    
    @Override
    public void store(MultipartFile file, String fileId) {
        log.info("Storing file {} to S3", fileId);
    }
    
    @Override
    public InputStream retrieve(String fileId) {
        log.info("Retrieving file {} from S3", fileId);
        return null;
    }
    
    @Override
    public void delete(String fileId) {
        log.info("Deleting file {} from S3", fileId);
    }
    
    @Override
    public boolean exists(String fileId) {
        log.info("Checking if file {} exists in S3", fileId);
        return false;
    }
}
package com.mendonca.testePratico.application.port.output;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingPort {
    void processAsync(MultipartFile file, String fileId);
    void cancel(String fileId);
}
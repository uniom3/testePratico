package com.mendonca.testePratico.application.port.output;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

public interface FileStoragePort {
    void store(MultipartFile file, String fileId);
    InputStream retrieve(String fileId);
    void delete(String fileId);
    boolean exists(String fileId);
}
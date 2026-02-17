package com.mendonca.testePratico.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(MultipartFile file) {}
package com.mendonca.testePratico.application.port.input;

import com.mendonca.testePratico.application.dto.request.FileUploadRequest;
import com.mendonca.testePratico.application.dto.response.UploadResponse;

public interface UploadFileUseCase {
    UploadResponse upload(FileUploadRequest request);
}
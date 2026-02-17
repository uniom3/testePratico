package com.mendonca.testePratico.application.port.input;

import com.mendonca.testePratico.application.dto.response.ProgressResponse;

public interface GetProgressUseCase {
    ProgressResponse getProgress(String fileId);
}
package com.mendonca.testePratico.application.port.input;

import com.mendonca.testePratico.application.dto.response.ResultResponse;

public interface GetResultUseCase {
    ResultResponse getResult(String fileId);
}
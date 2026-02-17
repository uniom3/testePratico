package com.mendonca.testePratico.application.dto.response;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import java.util.Map;

public record ResultResponse(
    String fileId,
    Map<String, Object> summary
) {
    public static ResultResponse from(FileProcessing processing) {
        return new ResultResponse(
            processing.getId().value(),
            processing.toSummary()
        );
    }
}
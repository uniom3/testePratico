package com.mendonca.testePratico.application.dto.response;

import com.mendonca.testePratico.domain.entity.FileProcessing;

public record ProgressResponse(
    String fileId,
    int progress,
    String status,
    String message
) {
    public static ProgressResponse from(FileProcessing processing) {
        String message = switch (processing.getStatus()) {
            case EM_PROCESSAMENTO -> "Processing in progress...";
            case FINALIZADO_COM_SUCESSO -> "Processing completed successfully";
            case FINALIZADO_COM_ERROS -> "Error: " + processing.getErrorMessage();
            default -> "Waiting to start processing";
        };
        
        return new ProgressResponse(
            processing.getId().value(),
            processing.getProgress().percentage(),
            processing.getStatus().name(),
            message
        );
    }
}
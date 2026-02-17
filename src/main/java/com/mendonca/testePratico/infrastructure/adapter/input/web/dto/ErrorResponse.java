package com.mendonca.testePratico.infrastructure.adapter.input.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String code;
    private String path;
    
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(error)
            .message(message)
            .path(path)
            .build();
    }
    
    public static ErrorResponse badRequest(String message, String path) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(400)
            .error("Bad Request")
            .message(message)
            .path(path)
            .build();
    }
    
    public static ErrorResponse notFound(String message, String path) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(404)
            .error("Not Found")
            .message(message)
            .path(path)
            .build();
    }
}
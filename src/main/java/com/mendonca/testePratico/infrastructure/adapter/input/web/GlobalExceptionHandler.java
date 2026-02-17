package com.mendonca.testePratico.infrastructure.adapter.input.web;

import com.mendonca.testePratico.domain.exception.InvalidFileException;
import com.mendonca.testePratico.domain.exception.ProcessingException;
import com.mendonca.testePratico.infrastructure.adapter.input.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFile(InvalidFileException ex) {
        log.error("Invalid file: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, "FILE-001");
    }
    
    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ErrorResponse> handleProcessing(ProcessingException ex) {
        log.error("Processing error: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "PROC-001");
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSize(MaxUploadSizeExceededException ex) {
        log.error("File too large: {}", ex.getMessage());
        return buildResponse("File size exceeds maximum allowed", HttpStatus.PAYLOAD_TOO_LARGE, "FILE-002");
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return buildResponse("Access denied", HttpStatus.FORBIDDEN, "AUTH-001");
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, "SYS-001");
    }
    
    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status, String code) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .code(code)
                .build();
        return new ResponseEntity<>(error, status);
    }
}
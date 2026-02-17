package com.mendonca.testePratico.infrastructure.adapter.input.web;

import com.mendonca.testePratico.application.dto.request.FileUploadRequest;
import com.mendonca.testePratico.application.dto.response.ProgressResponse;
import com.mendonca.testePratico.application.dto.response.ResultResponse;
import com.mendonca.testePratico.application.dto.response.UploadResponse;
import com.mendonca.testePratico.application.port.input.GetProgressUseCase;
import com.mendonca.testePratico.application.port.input.GetResultUseCase;
import com.mendonca.testePratico.application.port.input.UploadFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Processing", description = "Endpoints for file upload and processing")
public class FileProcessingController {
    
    private final UploadFileUseCase uploadFileUseCase;
    private final GetProgressUseCase getProgressUseCase;
    private final GetResultUseCase getResultUseCase;
    
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ENVIO')")
    @Operation(summary = "Upload a file for processing")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {
        
        FileUploadRequest request = new FileUploadRequest(file);
        UploadResponse response = uploadFileUseCase.upload(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/progress/{fileId}")
    @PreAuthorize("hasAnyRole('ENVIO', 'CONSULTA')")
    @Operation(summary = "Get processing progress")
    public ResponseEntity<ProgressResponse> getProgress(
            @PathVariable String fileId) {
        
        ProgressResponse response = getProgressUseCase.getProgress(fileId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/result/{fileId}")
    @PreAuthorize("hasRole('CONSULTA')")
    @Operation(summary = "Get processing result")
    public ResponseEntity<ResultResponse> getResult(
            @PathVariable String fileId) {
        
        ResultResponse response = getResultUseCase.getResult(fileId);
        return ResponseEntity.ok(response);
    }
}
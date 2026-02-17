package com.mendonca.testePratico.application.port.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mendonca.testePratico.application.dto.request.FileUploadRequest;
import com.mendonca.testePratico.application.dto.response.ProgressResponse;
import com.mendonca.testePratico.application.dto.response.ResultResponse;
import com.mendonca.testePratico.application.dto.response.UploadResponse;
import com.mendonca.testePratico.application.port.input.GetProgressUseCase;
import com.mendonca.testePratico.application.port.input.GetResultUseCase;
import com.mendonca.testePratico.application.port.input.UploadFileUseCase;
import com.mendonca.testePratico.application.port.output.FileProcessingPort;
import com.mendonca.testePratico.application.port.output.FileStoragePort;
import com.mendonca.testePratico.application.port.output.NotificationPort;
import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.exception.InvalidFileException;
import com.mendonca.testePratico.domain.exception.ProcessingException;
import com.mendonca.testePratico.domain.repository.FileProcessingRepository;
import com.mendonca.testePratico.domain.service.FileValidator;
import com.mendonca.testePratico.domain.service.ValidationResult;
import com.mendonca.testePratico.domain.vo.FileId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileProcessingService implements 
        UploadFileUseCase, 
        GetProgressUseCase, 
        GetResultUseCase {
    
    private final FileValidator fileValidator;
    private final FileProcessingRepository repository;
    private final FileProcessingPort testePratico;
    private final FileStoragePort fileStorage;
    private final NotificationPort notifier;
    
    @Override
    public UploadResponse upload(FileUploadRequest request) {
        log.info("Processing upload request for file: {}", request.file().getOriginalFilename());
    
    ValidationResult validation = fileValidator.validate(request.file());
    if (validation.isInvalid()) {
        throw new InvalidFileException(validation.getMessage());
    }
    
    String fileId = FileId.generate().value();
    FileProcessing fileProcessing = FileProcessing.create(
        fileId, 
        request.file().getOriginalFilename()
    );
    
    fileStorage.store(request.file(), fileId);
    
    fileProcessing.start();
    repository.save(fileProcessing);
    
    testePratico.processAsync(request.file(), fileId);
    
    notifier.notifyProgress(fileId, 0);
    
    log.info("File uploaded successfully with ID: {}", fileId);
    return UploadResponse.from(fileProcessing);
}

@Override
@Transactional(readOnly = true)
public ProgressResponse getProgress(String fileId) {
    log.debug("Getting progress for file: {}", fileId);
    
    FileProcessing processing = repository.findById(FileId.of(fileId))
        .orElseThrow(() -> new ProcessingException("File not found: " + fileId));
    
    return ProgressResponse.from(processing);
}

@Override
@Transactional(readOnly = true)
public ResultResponse getResult(String fileId) {
    log.debug("Getting result for file: {}", fileId);
    
    FileProcessing processing = repository.findById(FileId.of(fileId))
        .orElseThrow(() -> new ProcessingException("File not found: " + fileId));
    
    if (!processing.isComplete()) {
        throw new ProcessingException("Processing not completed yet");
        }
        
        return ResultResponse.from(processing);
    }
}
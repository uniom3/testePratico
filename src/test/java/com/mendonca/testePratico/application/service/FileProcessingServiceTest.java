package com.mendonca.testePratico.application.service;

import com.mendonca.testePratico.application.dto.request.FileUploadRequest;
import com.mendonca.testePratico.application.dto.response.ProgressResponse;
import com.mendonca.testePratico.application.dto.response.UploadResponse;
import com.mendonca.testePratico.application.port.output.FileProcessingPort;
import com.mendonca.testePratico.application.port.output.FileStoragePort;
import com.mendonca.testePratico.application.port.output.NotificationPort; 
import com.mendonca.testePratico.application.port.service.FileProcessingService;
import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.exception.InvalidFileException;
import com.mendonca.testePratico.domain.exception.ProcessingException;
import com.mendonca.testePratico.domain.repository.FileProcessingRepository;
import com.mendonca.testePratico.domain.service.FileValidator;
import com.mendonca.testePratico.domain.service.ValidationResult;
import com.mendonca.testePratico.domain.vo.FileId;
import com.mendonca.testePratico.domain.vo.Summary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FileProcessingService")
class FileProcessingServiceTest {
    
    @Mock
    private FileValidator fileValidator;
    
    @Mock
    private FileProcessingRepository repository;
    
    @Mock
    private FileProcessingPort fileProcessor;
    
    @Mock
    private FileStoragePort fileStorage;
    
    @Mock
    private NotificationPort notifier;  
    
    @InjectMocks
    private FileProcessingService service;
    
    @Captor
    private ArgumentCaptor<FileProcessing> fileProcessingCaptor;
    
    private MultipartFile validFile;
    private FileUploadRequest validRequest;
    private ValidationResult validResult;
    private ValidationResult invalidResult;
    
    @BeforeEach
    void setUp() {
        String content = "|0000|017|TESTE\n|0001|0|\n|0010|dado";
        validFile = new MockMultipartFile(
            "file", "test.txt", "text/plain", content.getBytes()
        );
        validRequest = new FileUploadRequest(validFile);
        
        validResult = ValidationResult.valid();
        invalidResult = ValidationResult.invalid("Erro de validação");
    }
    
    @Test
    @DisplayName("Deve fazer upload com sucesso")
    void shouldUploadSuccessfully() {
        when(fileValidator.validate(any())).thenReturn(validResult);
        
        UploadResponse response = service.upload(validRequest);
        
        assertNotNull(response);
        assertNotNull(response.fileId());
        assertEquals("PROCESSING", response.status());
        assertNotNull(response.progressUrl());
        
        verify(fileStorage).store(eq(validFile), anyString());
        verify(repository).save(fileProcessingCaptor.capture());
        verify(fileProcessor).processAsync(eq(validFile), anyString());
        verify(notifier).notifyProgress(anyString(), eq(0)); 
        
        FileProcessing saved = fileProcessingCaptor.getValue();
        assertEquals(validFile.getOriginalFilename(), saved.getFilename().value());
        assertEquals("PROCESSING", saved.getStatus().name());
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando arquivo é inválido")
    void shouldThrowExceptionWhenFileIsInvalid() {
        
        when(fileValidator.validate(any())).thenReturn(invalidResult);
        
        
        assertThrows(InvalidFileException.class, () -> service.upload(validRequest));
        
        verify(fileStorage, never()).store(any(), any());
        verify(repository, never()).save(any());
        verify(fileProcessor, never()).processAsync(any(), any());
        verify(notifier, never()).notifyProgress(anyString(), anyInt());  
    }
    
    @Test
    @DisplayName("Deve consultar progresso com sucesso")
    void shouldGetProgressSuccessfully() {
        
        String fileId = "123e4567-e89b-12d3-a456-426614174000";
        FileProcessing processing = FileProcessing.create(fileId, "test.txt");
        processing.start();
        
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.of(processing));
        
        
        ProgressResponse response = service.getProgress(fileId);
        
        
        assertNotNull(response);
        assertEquals(fileId, response.fileId());
        assertEquals(0, response.progress());
        assertEquals("PROCESSING", response.status());
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao consultar progresso de arquivo inexistente")
    void shouldThrowExceptionWhenGettingProgressForNonExistentFile() {
        
        String fileId = "inexistente";
        
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.empty());
        
        
        assertThrows(ProcessingException.class, () -> service.getProgress(fileId));
    }
    
    @Test
    @DisplayName("Deve consultar resultado com sucesso quando completo")
    void shouldGetResultSuccessfullyWhenComplete() {
        
        String fileId = "123e4567-e89b-12d3-a456-426614174000";
        FileProcessing processing = FileProcessing.create(fileId, "test.txt");
        processing.start();
        
        Summary summary = new Summary.Builder()
            .addRecord("001", 100.0)
            .build();
        processing.complete(summary);
        
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.of(processing));
        
        
        var response = service.getResult(fileId);
        
        
        assertNotNull(response);
        assertEquals(fileId, response.fileId());
        assertNotNull(response.summary());
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao consultar resultado de arquivo em processamento")
    void shouldThrowExceptionWhenGettingResultForProcessingFile() {
        
        String fileId = "123e4567-e89b-12d3-a456-426614174000";
        FileProcessing processing = FileProcessing.create(fileId, "test.txt");
        processing.start();
        
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.of(processing));
        
        
        assertThrows(ProcessingException.class, () -> service.getResult(fileId));
    }
}
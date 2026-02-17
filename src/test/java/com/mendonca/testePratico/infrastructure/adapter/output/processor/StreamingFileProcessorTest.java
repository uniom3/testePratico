package com.mendonca.testePratico.infrastructure.adapter.output.processor;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.repository.FileProcessingRepository;
import com.mendonca.testePratico.domain.service.RecordParser;
import com.mendonca.testePratico.domain.service.StatisticsCalculator;
import com.mendonca.testePratico.domain.vo.FileId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do StreamingFileProcessor")
class StreamingFileProcessorTest {
    
    @Mock
    private FileProcessingRepository repository;
    
    @Mock
    private RecordParser recordParser;
    
    @Mock
    private StatisticsCalculator calculator;
    
    @InjectMocks
    private StreamingFileProcessor processor;
    
    private String fileId;
    private MultipartFile file;
    private FileProcessing fileProcessing;
    
    @BeforeEach
    void setUp() {
        fileId = "123e4567-e89b-12d3-a456-426614174000";
        String content = "|0000|017|TESTE\n|0001|0|\n|0010|dado1\n|0020|dado2\n";
        file = new MockMultipartFile(
            "file", "test.txt", "text/plain", content.getBytes()
        );
        
        fileProcessing = FileProcessing.create(fileId, "test.txt");
    }
    
    @Test
    @DisplayName("Deve processar arquivo com sucesso")
    void shouldProcessFileSuccessfully() {
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.of(fileProcessing));
        
        processor.processAsync(file, fileId);
        
        verify(repository, atLeastOnce()).save(any(FileProcessing.class));
    }
    
    @Test
    @DisplayName("Deve lidar com erro quando arquivo não encontrado")
    void shouldHandleErrorWhenFileNotFound() {
        when(repository.findById(FileId.of(fileId)))
            .thenReturn(Optional.empty());
        
        processor.processAsync(file, fileId);
        
        verify(repository, never()).save(any(FileProcessing.class));
    }
}

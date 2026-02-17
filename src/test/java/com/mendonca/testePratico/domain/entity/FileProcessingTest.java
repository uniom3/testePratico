package com.mendonca.testePratico.domain.entity;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import com.mendonca.testePratico.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade FileProcessing")
class FileProcessingTest {
    
    private FileProcessing fileProcessing;
    private String fileId;
    private String filename;
    
    @BeforeEach
    void setUp() {
        fileId = "123e4567-e89b-12d3-a456-426614174000";
        filename = "teste.txt";
        fileProcessing = FileProcessing.create(fileId, filename);
    }
    
    @Test
    @DisplayName("Deve criar uma entidade com status PENDING")
    void shouldCreateEntityWithPendingStatus() {
        assertNotNull(fileProcessing);
        assertEquals(fileId, fileProcessing.getId().value());
        assertEquals(filename, fileProcessing.getFilename().value());
        assertEquals(ProcessingStatus.PENDING, fileProcessing.getStatus());
        assertEquals(0, fileProcessing.getProgress().percentage());
        assertNotNull(fileProcessing.getAuditInfo());
        assertNotNull(fileProcessing.getAuditInfo().getCreatedAt());
    }
    
    @Test
    @DisplayName("Deve iniciar processamento com sucesso")
    void shouldStartProcessing() {
        fileProcessing.start();
        
        assertEquals(ProcessingStatus.PROCESSING, fileProcessing.getStatus());
        assertEquals(0, fileProcessing.getProgress().percentage());
        assertNotNull(fileProcessing.getAuditInfo().getStartedAt());
    }
    
    
    @Test
    @DisplayName("Deve atualizar progresso corretamente")
    void shouldUpdateProgress() {
        fileProcessing.start();
        fileProcessing.updateProgress(50);
        
        assertEquals(50, fileProcessing.getProgress().percentage());
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar progresso sem iniciar")
    void shouldThrowExceptionWhenUpdatingProgressWithoutStart() {
        assertThrows(ProcessingException.class, () -> fileProcessing.updateProgress(50));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar progresso com valor inválido")
    void shouldThrowExceptionWhenUpdatingProgressWithInvalidValue() {
        fileProcessing.start();
        
        assertThrows(ProcessingException.class, () -> fileProcessing.updateProgress(-1));
        assertThrows(ProcessingException.class, () -> fileProcessing.updateProgress(101));
    }
    
    @Test
    @DisplayName("Deve completar processamento com sucesso")
    void shouldCompleteProcessing() {
        Summary summary = new Summary.Builder()
            .addRecord("001", 100.0)
            .addRecord("002", 200.0)
            .build();
        
        fileProcessing.start();
        fileProcessing.complete(summary);
        
        assertEquals(ProcessingStatus.COMPLETED, fileProcessing.getStatus());
        assertEquals(100, fileProcessing.getProgress().percentage());
        assertNotNull(fileProcessing.getResultSummary());
        assertNotNull(fileProcessing.getAuditInfo().getCompletedAt());
    }
    
    @Test
    @DisplayName("Deve falhar processamento com erro")
    void shouldFailProcessing() {
        String errorMessage = "Erro no processamento";
        
        fileProcessing.fail(errorMessage);
        
        assertEquals(ProcessingStatus.ERROR, fileProcessing.getStatus());
        assertEquals(errorMessage, fileProcessing.getErrorMessage());
    }
    
    @Test
    @DisplayName("Deve verificar se está completo")
    void shouldCheckIfComplete() {
        assertFalse(fileProcessing.isComplete());
        
        fileProcessing.start();
        assertFalse(fileProcessing.isComplete());
        
        Summary summary = new Summary.Builder().build();
        fileProcessing.complete(summary);
        assertTrue(fileProcessing.isComplete());
    }
    
    @Test
    @DisplayName("Deve verificar se tem erro")
    void shouldCheckIfHasError() {
        assertFalse(fileProcessing.hasError());
        
        fileProcessing.fail("Erro");
        assertTrue(fileProcessing.hasError());
    }
    
    @Test
    @DisplayName("Deve gerar sumário corretamente")
    void shouldGenerateSummary() {
        fileProcessing.start();
        
        Summary summary = new Summary.Builder()
            .addRecord("001", 100.0)
            .addRecord("001", 200.0)
            .addRecord("002", 300.0)
            .build();
        
        fileProcessing.complete(summary);
        
        var result = fileProcessing.toSummary();
        
        assertEquals(fileId, result.get("id"));
        assertEquals(filename, result.get("filename"));
        assertEquals("COMPLETED", result.get("status"));
        assertEquals(100, result.get("progress"));
        assertNotNull(result.get("result"));
    }
}

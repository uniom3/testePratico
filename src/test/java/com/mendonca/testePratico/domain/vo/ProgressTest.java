package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do Value Object Progress")
class ProgressTest {
    
    @Test
    @DisplayName("Deve criar progresso inicial")
    void shouldCreateInitialProgress() {
        Progress progress = Progress.initial();
        
        assertEquals(0, progress.percentage());
    }
    
    @Test
    @DisplayName("Deve criar progresso completo")
    void shouldCreateCompleteProgress() {
        Progress progress = Progress.complete();
        
        assertEquals(100, progress.percentage());
    }
    
    @Test
    @DisplayName("Deve criar progresso com valor específico")
    void shouldCreateProgressWithValue() {
        Progress progress = Progress.of(75);
        
        assertEquals(75, progress.percentage());
    }
    
    @Test
    @DisplayName("Deve lançar exceção para progresso inválido")
    void shouldThrowExceptionForInvalidProgress() {
        assertThrows(ProcessingException.class, () -> Progress.of(-1));
        assertThrows(ProcessingException.class, () -> Progress.of(101));
    }
}

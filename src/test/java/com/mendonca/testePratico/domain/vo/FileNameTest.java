package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do Value Object FileName")
class FileNameTest {
    
    @Test
    @DisplayName("Deve criar FileName válido")
    void shouldCreateValidFileName() {
        String name = "arquivo.txt";
        FileName fileName = FileName.of(name);
        
        assertEquals(name, fileName.value());
    }
    
    @Test
    @DisplayName("Deve lançar exceção para nome nulo")
    void shouldThrowExceptionForNullName() {
        assertThrows(ProcessingException.class, () -> FileName.of(null));
    }
    
    @Test
    @DisplayName("Deve lançar exceção para nome vazio")
    void shouldThrowExceptionForEmptyName() {
        assertThrows(ProcessingException.class, () -> FileName.of(""));
        assertThrows(ProcessingException.class, () -> FileName.of("   "));
    }
    
    @Test
    @DisplayName("Deve lançar exceção para nome muito longo")
    void shouldThrowExceptionForTooLongName() {
        String longName = "a".repeat(256);
        assertThrows(ProcessingException.class, () -> FileName.of(longName));
    }
}
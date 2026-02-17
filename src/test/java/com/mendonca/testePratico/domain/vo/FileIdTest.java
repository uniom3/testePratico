package com.mendonca.testePratico.domain.vo;

import com.mendonca.testePratico.domain.exception.ProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do Value Object FileId")
class FileIdTest {
    
    @Test
    @DisplayName("Deve criar FileId válido")
    void shouldCreateValidFileId() {
        String value = "123e4567-e89b-12d3-a456-426614174000";
        FileId fileId = FileId.of(value);
        
        assertEquals(value, fileId.value());
    }
    
    @Test
    @DisplayName("Deve gerar FileId automaticamente")
    void shouldGenerateFileId() {
        FileId fileId = FileId.generate();
        
        assertNotNull(fileId);
        assertNotNull(fileId.value());
        assertTrue(fileId.value().matches(
            "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"
        ));
    }
    
    @Test
    @DisplayName("Deve lançar exceção para FileId nulo")
    void shouldThrowExceptionForNullId() {
        assertThrows(ProcessingException.class, () -> FileId.of(null));
    }
    
    @Test
    @DisplayName("Deve lançar exceção para FileId vazio")
    void shouldThrowExceptionForEmptyId() {
        assertThrows(ProcessingException.class, () -> FileId.of(""));
        assertThrows(ProcessingException.class, () -> FileId.of("   "));
    }
}

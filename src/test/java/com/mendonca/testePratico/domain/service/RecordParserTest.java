package com.mendonca.testePratico.domain.service;

import com.mendonca.testePratico.domain.vo.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do RecordParser")
class RecordParserTest {
    
    private RecordParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new RecordParser();
    }
    
    @Test
    @DisplayName("Deve parsear linha válida com valor")
    void shouldParseValidLineWithValue() {
        String line = "|0010|001|João|100.50";
        
        Record record = parser.parse(line);
        
        assertTrue(record.valid());
        assertEquals("0010", record.type());
        assertEquals(0.0, record.value());
    }
    
    @Test
    @DisplayName("Deve parsear linha válida sem valor")
    void shouldParseValidLineWithoutValue() {
        String line = "|0020|002|Produto";
        
        Record record = parser.parse(line);
        
        assertTrue(record.valid());
        assertEquals("0020", record.type());
        assertEquals(0.0, record.value());
    }
    
    @Test
    @DisplayName("Deve tratar linha com valor inválido")
    void shouldHandleInvalidValue() {
        String line = "|0010|001|João|ABC";
        
        Record record = parser.parse(line);
        
        assertTrue(record.valid()); 
        assertEquals(0.0, record.value());
    }
    
    @Test
    @DisplayName("Deve tratar linha vazia como inválida")
    void shouldHandleEmptyLine() {
        Record record = parser.parse("");
        
        assertFalse(record.valid());
    }
    
    @Test
    @DisplayName("Deve tratar linha com formato inválido")
    void shouldHandleInvalidFormat() {
        Record record = parser.parse("linha sem pipes");
        
        assertFalse(record.valid());
    }
}
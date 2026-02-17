package com.mendonca.testePratico.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mendonca.testePratico.domain.exception.InvalidFileException;
import com.mendonca.testePratico.shared.constant.FileConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FileValidator")
class FileValidatorTest {
    
    private FileValidator fileValidator;
    
    @BeforeEach
    void setUp() {
        fileValidator = new FileValidator();
    }
    
    @Test
    @DisplayName("Deve validar arquivo com cabeçalho 017 e segunda linha correta")
    void shouldValidateFileWithHeader017() {
        String content = "|0000|017|CADASTRO|20240217\n|0001|0|\n|0010|001|João|100.50";
        MultipartFile file = createMockFile("valid017.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }
    
    @Test
    @DisplayName("Deve validar arquivo com cabeçalho 006 e segunda linha correta")
    void shouldValidateFileWithHeader006() {
        String content = "|0000|006|RELATORIO|20240217\n|0001|0|\n|0010|001|Maria|200.50";
        MultipartFile file = createMockFile("valid006.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }
    
    @Test
    @DisplayName("Deve rejeitar arquivo com cabeçalho inválido")
    void shouldRejectInvalidHeader() {
        String content = "|9999|999|INVALIDO\n|0001|0|\n|0010|001|dado";
        MultipartFile file = createMockFile("invalid.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("start with"));
        assertEquals("File must start with |0000|017| or |0000|006|", result.getMessage());
    }
    
    @Test
    @DisplayName("Deve rejeitar arquivo com apenas uma linha")
    void shouldRejectFileWithOnlyOneLine() {
        String content = "|0000|017|TESTE";
        MultipartFile file = createMockFile("one_line.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("Second line"));
    }
    
    @Test
    @DisplayName("Deve rejeitar arquivo vazio")
    void shouldRejectEmptyFile() {
        String content = "";
        MultipartFile file = createMockFile("empty.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("Deve rejeitar arquivo com primeira linha nula")
    void shouldRejectNullFirstLine() {
        MultipartFile file = mock(MultipartFile.class);
        InputStream mockStream = new ByteArrayInputStream(new byte[0]);
        
        try {
            when(file.getInputStream()).thenReturn(mockStream);
        } catch (IOException e) {
            fail("Mock setup failed");
        }
        
        ValidationResult result = fileValidator.validate(file);
        
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("start with"));
    }
    
    @Test
    @DisplayName("Deve lançar InvalidFileException quando ocorrer erro de leitura")
    void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Erro de leitura simulado"));
        
        InvalidFileException exception = assertThrows(
            InvalidFileException.class,
            () -> fileValidator.validate(file)
        );
        
        assertTrue(exception.getMessage().contains("Error reading file"));
    }
    
    @Test
    @DisplayName("Deve aceitar arquivo com espaços em branco na segunda linha")
    void shouldAcceptFileWithSpacesInSecondLine() {
        String content = "|0000|017|TESTE\n   |0001|0|   \n|0010|001|dado";
        MultipartFile file = createMockFile("with_spaces.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }
    
    @Test
    @DisplayName("Deve aceitar arquivo com linhas adicionais após cabeçalho")
    void shouldAcceptFileWithManyLines() {
        StringBuilder content = new StringBuilder();
        content.append("|0000|017|TESTE\n");
        content.append("|0001|0|\n");
        for (int i = 0; i < 1000; i++) {
            content.append("|0010|").append(String.format("%03d", i)).append("|dado\n");
        }
        
        MultipartFile file = createMockFile("many_lines.txt", content.toString());
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }
    
    @Test
    @DisplayName("Deve validar arquivo com caracteres especiais")
    void shouldValidateFileWithSpecialCharacters() {
        String content = "|0000|017|TESTE-ÇÃÂÁ@#$%\n|0001|0|\n|0010|001|João e Maria|100.50";
        MultipartFile file = createMockFile("special.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }
    
    @Test
    @DisplayName("Deve validar arquivo com constantes do FileConstants")
    void shouldUseCorrectConstants() {
        String content = FileConstants.HEADER_017 + "TESTE\n" + FileConstants.SECOND_LINE + "\ndata";
        MultipartFile file = createMockFile("constants.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertTrue(result.isValid());
    }
    
    @Test
    @DisplayName("Deve rejeitar arquivo com segunda linha exata mas com caracteres extras")
    void shouldRejectExactSecondLineWithExtraChars() {
        String content = "|0000|017|TESTE\n|0001|0|extra\n|0010|dado";
        MultipartFile file = createMockFile("extra.txt", content);
        
        ValidationResult result = fileValidator.validate(file);
        
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("Second line must be exactly"));
    }
    
    private MultipartFile createMockFile(String filename, String content) {
        return new MockMultipartFile(
            "file",
            filename,
            "text/plain",
            content.getBytes()
        );
    }
}
package com.mendonca.testePratico.domain.service;

import com.mendonca.testePratico.domain.exception.InvalidFileException;
import com.mendonca.testePratico.shared.constant.FileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class FileValidator {
    
    public ValidationResult validate(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
            
            String firstLine = reader.readLine();
            String secondLine = reader.readLine();
            
            if (!isValidHeader(firstLine)) {
                return ValidationResult.invalid(
                    "File must start with |0000|017| or |0000|006|"
                );
            }
            
            if (!isValidSecondLine(secondLine)) {
                return ValidationResult.invalid(
                    "Second line must be exactly |0001|0|"
                );
            }
            
            return ValidationResult.valid();
            
        } catch (IOException e) {
            log.error("Error validating file", e);
            throw new InvalidFileException("Error reading file: " + e.getMessage());
        }
    }
    
    private boolean isValidHeader(String firstLine) {
        if (firstLine == null) return false;
        return firstLine.startsWith(FileConstants.HEADER_017) || 
               firstLine.startsWith(FileConstants.HEADER_006);
    }
    
    private boolean isValidSecondLine(String secondLine) {
        return secondLine != null && 
               FileConstants.SECOND_LINE.equals(secondLine.trim());
    }
    
}
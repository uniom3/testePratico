package com.mendonca.testePratico.domain.repository;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.vo.FileId;

import java.util.Optional;

public interface FileProcessingRepository {
    FileProcessing save(FileProcessing fileProcessing);
    Optional<FileProcessing> findById(FileId id);
    void deleteById(FileId id);
    boolean existsById(FileId id);
}
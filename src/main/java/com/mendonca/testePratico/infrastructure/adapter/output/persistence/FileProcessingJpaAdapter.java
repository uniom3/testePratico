package com.mendonca.testePratico.infrastructure.adapter.output.persistence;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.mendonca.testePratico.domain.repository.FileProcessingRepository;
import com.mendonca.testePratico.domain.vo.FileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FileProcessingJpaAdapter implements FileProcessingRepository {
    
    private final FileProcessingJpaRepository jpaRepository;
    private final FileProcessingMapper mapper;
    
    @Override
    public FileProcessing save(FileProcessing fileProcessing) {
        FileProcessingEntity entity = mapper.toEntity(fileProcessing);
        FileProcessingEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<FileProcessing> findById(FileId id) {
        return jpaRepository.findById(id.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public void deleteById(FileId id) {
        jpaRepository.deleteById(id.value());
    }
    
    @Override
    public boolean existsById(FileId id) {
        return jpaRepository.existsById(id.value());
    }
}
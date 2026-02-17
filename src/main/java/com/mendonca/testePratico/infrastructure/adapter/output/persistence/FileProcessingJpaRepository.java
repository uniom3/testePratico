package com.mendonca.testePratico.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileProcessingJpaRepository extends JpaRepository<FileProcessingEntity, String> {
}
package ru.itegor.antiplagiacode.comparison_result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComparisonResultRepository extends JpaRepository<ComparisonResultEntity, Long> {
    List<ComparisonResultEntity> findAllByOriginalFile_Id(Long id);

    Optional<ComparisonResultEntity> findByOriginalFile_IdAndComparedFile_Id(Long originalFileId, Long comparedFileId);
}
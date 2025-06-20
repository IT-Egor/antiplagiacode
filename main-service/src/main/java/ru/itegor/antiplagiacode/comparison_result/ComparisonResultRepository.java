package ru.itegor.antiplagiacode.comparison_result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComparisonResultRepository extends JpaRepository<ComparisonResultEntity, Long> {
    List<ComparisonResultEntity> findAllByOriginalFile_Id(Long id);
}
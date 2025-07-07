package ru.itegor.antiplagiacode.comparison_result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComparisonResultRepository extends JpaRepository<ComparisonResultEntity, Long> {
    List<ComparisonResultEntity> findAllByOriginalFile_Id(Long id);

    List<ComparisonResultEntity> findAllByOriginalFile_Task_Id(Long taskId);

    @Query("SELECT c FROM ComparisonResultEntity c WHERE c.originalFile.id = :fileId AND c.result >= :threshold")
    List<ComparisonResultEntity> findWarningsByOriginalFileId(@Param("fileId") Long fileId,
                                                              @Param("threshold") Double threshold);

    @Query("SELECT c FROM ComparisonResultEntity c WHERE c.originalFile.task.id = :taskId AND c.result >= :threshold")
    List<ComparisonResultEntity> findWarningsByTaskId(@Param("taskId") Long taskId,
                                                      @Param("threshold") Double threshold);
}
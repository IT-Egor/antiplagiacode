package ru.itegor.antiplagiacode.comparison_result.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;
import ru.itegor.antiplagiacode.comparison_result.dto.MergeComparisonResultRequestDto;

import java.util.List;

public interface ComparisonResultService {
    Page<ComparisonResultResponseDto> getAll(Pageable pageable);

    ComparisonResultResponseDto getOne(Long id);

    List<ComparisonResultResponseDto> getAllByFileId(Long id);

    List<ComparisonResultResponseDto> getAllByTaskId(Long taskId);

    List<ComparisonResultResponseDto> mergeMany(MergeComparisonResultRequestDto dto);

    List<ComparisonResultResponseDto> getWarningsByFileId(Long fileId);

    List<ComparisonResultResponseDto> getWarningsByTaskId(Long taskId);

    void deleteManyByOriginalFileId(Long id);

    void deleteMany(List<Long> ids);
}

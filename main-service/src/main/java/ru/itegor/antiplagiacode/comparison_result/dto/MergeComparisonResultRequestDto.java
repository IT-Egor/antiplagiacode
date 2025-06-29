package ru.itegor.antiplagiacode.comparison_result.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class MergeComparisonResultRequestDto {
    @NotNull(message = "Original file id is required")
    Long originalFileId;

    @Valid
    List<ComparisonResultDto> comparisonResults;
}
package ru.itegor.antiplagiacode.comparison_result.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.itegor.antiplagiacode.comparison_result.ComparisonResultEntity}
 */
@Value
public class CreateComparisonResultRequestDto {
    @NotNull(message = "Comparison result is required")
    BigDecimal result;

    @NotNull(message = "Original file id is required")
    Long originalFileId;

    @NotNull(message = "Compared file id is required")
    Long comparedFileId;
}
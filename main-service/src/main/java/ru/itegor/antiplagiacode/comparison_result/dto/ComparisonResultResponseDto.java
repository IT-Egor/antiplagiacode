package ru.itegor.antiplagiacode.comparison_result.dto;

import lombok.Value;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.itegor.antiplagiacode.comparison_result.ComparisonResultEntity}
 */
@Value
public class ComparisonResultResponseDto {
    Integer id;
    BigDecimal result;
    Long originalFileId;
    Long comparedFileId;
}
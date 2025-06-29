package ru.itegor.antiplagiacode.comparison_result.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class ComparisonResultDto {
    @NotNull(message = "Result is required")
    BigDecimal result;

    @NotNull(message = "Compared file id is required")
    Long comparedFileId;
}

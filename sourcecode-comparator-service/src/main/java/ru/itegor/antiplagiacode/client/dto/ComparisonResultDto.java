package ru.itegor.antiplagiacode.client.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ComparisonResultDto {
    BigDecimal result;
    Long comparedFileId;
}

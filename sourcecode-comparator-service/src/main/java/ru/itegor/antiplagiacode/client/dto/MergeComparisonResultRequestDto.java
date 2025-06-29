package ru.itegor.antiplagiacode.client.dto;

import lombok.Value;

import java.util.List;


@Value
public class MergeComparisonResultRequestDto {
    Long originalFileId;
    List<ComparisonResultDto> comparisonResults;
}
package ru.itegor.antiplagiacode.comparison_result.dto;

import lombok.Value;

import java.util.List;

@Value
public class ComparisonResultMessage {
    Long originalFileId;
    List<Long> comparedFileIds;
}

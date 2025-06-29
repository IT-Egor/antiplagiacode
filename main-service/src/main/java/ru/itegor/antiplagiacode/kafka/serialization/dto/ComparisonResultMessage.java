package ru.itegor.antiplagiacode.kafka.serialization.dto;

import lombok.Value;

import java.util.List;

@Value
public class ComparisonResultMessage {
    Long originalFileId;
    List<Long> comparedFileIds;
}

package ru.itegor.antiplagiacode.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonResultMessage {
    private Long originalFileId;
    private List<Long> comparedFileIds;
}

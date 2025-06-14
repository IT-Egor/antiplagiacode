package ru.itegor.antiplagiacode.task.dto;

import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link ru.itegor.antiplagiacode.task.TaskEntity}
 */
@Value
public class TaskResponseDto {
    Long id;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    Long classId;
}
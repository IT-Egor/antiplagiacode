package ru.itegor.antiplagiacode.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.itegor.antiplagiacode.validation_groups.CreateValidation;
import ru.itegor.antiplagiacode.validation_groups.UpdateValidation;

import java.time.LocalDate;

/**
 * DTO for {@link ru.itegor.antiplagiacode.task.TaskEntity}
 */
@Value
@Schema(description = "Task create and update DTO")
public class MergeTaskRequestDto {
    @NotNull(message = "Task name is required", groups = {CreateValidation.class})
    String name;

    @NotNull(message = "Task start date is required", groups = {CreateValidation.class})
    @FutureOrPresent(message = "Task start date must be future or present",
            groups = {CreateValidation.class, UpdateValidation.class})
    LocalDate startDate;

    @NotNull(message = "Task end date is required", groups = {CreateValidation.class})
    @Future(message = "Task end date must be future", groups = {CreateValidation.class, UpdateValidation.class})
    LocalDate endDate;

    String description;

    @NotNull(message = "Class id is required", groups = {CreateValidation.class})
    Long classId;
}
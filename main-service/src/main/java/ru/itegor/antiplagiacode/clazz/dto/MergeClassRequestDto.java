package ru.itegor.antiplagiacode.clazz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for {@link ru.itegor.antiplagiacode.clazz.ClassEntity}
 */
@Value
@Schema(description = "Class create and update DTO")
public class MergeClassRequestDto {
    @NotBlank(message = "Class name is required")
    String name;
}
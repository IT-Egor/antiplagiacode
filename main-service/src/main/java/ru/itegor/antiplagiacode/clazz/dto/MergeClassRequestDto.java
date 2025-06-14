package ru.itegor.antiplagiacode.clazz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for {@link ru.itegor.antiplagiacode.clazz.ClassEntity}
 */
@Value
public class MergeClassRequestDto {
    @NotBlank(message = "Class name is required")
    String name;
}
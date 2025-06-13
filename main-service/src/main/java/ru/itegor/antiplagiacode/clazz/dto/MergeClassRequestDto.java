package ru.itegor.antiplagiacode.clazz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for {@link ru.itegor.antiplagiacode.clazz.ClassEntity}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeClassRequestDto {
    @NotBlank
    private String name;
}
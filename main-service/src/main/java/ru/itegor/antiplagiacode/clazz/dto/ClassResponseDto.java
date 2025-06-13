package ru.itegor.antiplagiacode.clazz.dto;

import lombok.*;

/**
 * DTO for {@link ru.itegor.antiplagiacode.clazz.ClassEntity}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassResponseDto {
    private Long id;
    private String name;
}
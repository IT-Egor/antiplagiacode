package ru.itegor.antiplagiacode.clazz.dto;

import lombok.*;

/**
 * DTO for {@link ru.itegor.antiplagiacode.clazz.ClassEntity}
 */
@Value
public class ClassResponseDto {
    Long id;
    String name;
}
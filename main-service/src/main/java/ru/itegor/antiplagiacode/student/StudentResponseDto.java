package ru.itegor.antiplagiacode.student;

import lombok.*;

/**
 * DTO for {@link StudentClassEntity}
 */
@Value
public class StudentResponseDto {
    Long id;
    Long studentId;
    Long classId;
}
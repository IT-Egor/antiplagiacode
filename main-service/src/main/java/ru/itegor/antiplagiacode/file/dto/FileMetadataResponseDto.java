package ru.itegor.antiplagiacode.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.itegor.antiplagiacode.file.FileEntity}
 */
@Value
public class FileMetadataResponseDto {
    Long id;

    @Schema(description = "File name in storage", example = "task-id/1/student-id/1/test1.py")
    String objectName;

    LocalDateTime uploadTimestamp;
    Long fileSizeByte;
    Long studentId;
    Long taskId;
}
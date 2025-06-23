package ru.itegor.antiplagiacode.file.dto;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.itegor.antiplagiacode.file.FileEntity}
 */
@Value
public class FileMetadataResponseDto {
    Long id;
    String objectName;
    LocalDateTime uploadTimestamp;
    Long fileSizeByte;
    Long studentId;
    Long taskId;
}
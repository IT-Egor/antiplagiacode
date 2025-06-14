package ru.itegor.antiplagiacode.file.dto;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.itegor.antiplagiacode.file.FileEntity}
 */
@Value
public class FileResponseDto {
    Long id;
    String storageId;
    String filename;
    LocalDateTime uploadDate;
    Integer fileSizeByte;
    Long studentId;
    Long taskId;
}
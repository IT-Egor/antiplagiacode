package ru.itegor.antiplagiacode.file.dto;

import lombok.Value;

@Value
public class FileResponseDto {
    String fileName;
    byte[] file;
}

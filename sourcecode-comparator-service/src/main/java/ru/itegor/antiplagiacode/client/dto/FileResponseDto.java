package ru.itegor.antiplagiacode.client.dto;

import lombok.Value;

@Value
public class FileResponseDto {
    String fileName;
    byte[] file;
}

package ru.itegor.antiplagiacode.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    @Schema(description = "Error message", example = "Unable to execute SQL statement")
    private final String message;

    @Schema(description = "Error reason", example = "Data integrity violation")
    private final String reason;

    @Schema(description = "Error status", example = "400")
    private final int status;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
}

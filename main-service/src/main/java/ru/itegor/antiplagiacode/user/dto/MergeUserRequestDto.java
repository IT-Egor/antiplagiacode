package ru.itegor.antiplagiacode.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.itegor.antiplagiacode.user.UserEntity;

/**
 * DTO for {@link ru.itegor.antiplagiacode.user.UserEntity}
 */
@Value
public class MergeUserRequestDto {
    @NotBlank(message = "Username is required")
    String username;

    @NotBlank(message = "Password is required")
    String password;

    @NotNull(message = "Role is required")
    UserEntity.Role role;
}
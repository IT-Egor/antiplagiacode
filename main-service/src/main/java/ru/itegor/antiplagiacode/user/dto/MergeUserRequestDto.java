package ru.itegor.antiplagiacode.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.itegor.antiplagiacode.user.UserEntity;

/**
 * DTO for {@link ru.itegor.antiplagiacode.user.UserEntity}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeUserRequestDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull
    private UserEntity.Role role;
}
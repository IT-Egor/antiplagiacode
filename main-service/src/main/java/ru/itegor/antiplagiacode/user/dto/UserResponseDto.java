package ru.itegor.antiplagiacode.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itegor.antiplagiacode.user.UserEntity;

/**
 * DTO for {@link ru.itegor.antiplagiacode.user.UserEntity}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private UserEntity.Role role;
}
package ru.itegor.antiplagiacode.user.dto;

import lombok.*;
import ru.itegor.antiplagiacode.user.UserEntity;

/**
 * DTO for {@link ru.itegor.antiplagiacode.user.UserEntity}
 */
@Value
public class UserResponseDto {
    Long id;
    String username;
    UserEntity.Role role;
}
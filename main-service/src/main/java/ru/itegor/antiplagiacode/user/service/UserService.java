package ru.itegor.antiplagiacode.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    Page<UserResponseDto> getAll(Pageable pageable);

    UserResponseDto getOne(Long id);

    List<UserResponseDto> getMany(List<Long> ids);

    UserResponseDto create(MergeUserRequestDto dto);

    UserResponseDto patch(Long id, MergeUserRequestDto dto);

    UserResponseDto delete(Long id);

    void deleteMany(List<Long> ids);
}

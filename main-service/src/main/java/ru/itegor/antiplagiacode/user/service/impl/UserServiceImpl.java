package ru.itegor.antiplagiacode.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.user.UserEntity;
import ru.itegor.antiplagiacode.user.UserMapper;
import ru.itegor.antiplagiacode.user.UserRepository;
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;
import ru.itegor.antiplagiacode.user.service.UserService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserResponseDto);
    }

    @Override
    public UserResponseDto getOne(Long id) {
        return userMapper.toUserResponseDto(findById(id));
    }

    @Override
    public List<UserResponseDto> getMany(List<Long> ids) {
        List<UserEntity> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto create(MergeUserRequestDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        UserEntity resultUser = userRepository.save(user);
        return userMapper.toUserResponseDto(resultUser);
    }

    @Override
    public UserResponseDto patch(Long id, MergeUserRequestDto dto) {
        UserEntity user = findById(id);

        userMapper.updateWithNull(dto, user);

        UserEntity resultUser = userRepository.save(user);
        return userMapper.toUserResponseDto(resultUser);
    }

    @Override
    public UserResponseDto delete(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    private UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id `%s` not found".formatted(id)));
    }
}

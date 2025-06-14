package ru.itegor.antiplagiacode.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itegor.antiplagiacode.user.UserEntity;
import ru.itegor.antiplagiacode.user.UserMapper;
import ru.itegor.antiplagiacode.user.UserRepository;
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;
import ru.itegor.antiplagiacode.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(userMapper::toUserResponseDto);
    }

    @Override
    public UserResponseDto getOne(Long id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        return userMapper.toUserResponseDto(userEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @Override
    public List<UserResponseDto> getMany(List<Long> ids) {
        List<UserEntity> userEntities = userRepository.findAllById(ids);
        return userEntities.stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto create(MergeUserRequestDto dto) {
        UserEntity userEntity = userMapper.toEntity(dto);
        UserEntity resultUserEntity = userRepository.save(userEntity);
        return userMapper.toUserResponseDto(resultUserEntity);
    }

    @Override
    public UserResponseDto patch(Long id, MergeUserRequestDto dto) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        userMapper.updateWithNull(dto, userEntity);

        UserEntity resultUserEntity = userRepository.save(userEntity);
        return userMapper.toUserResponseDto(resultUserEntity);
    }

    @Override
    public UserResponseDto delete(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            userRepository.delete(userEntity);
        }
        return userMapper.toUserResponseDto(userEntity);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
}

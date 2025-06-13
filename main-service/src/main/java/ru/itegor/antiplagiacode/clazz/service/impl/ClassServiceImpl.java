package ru.itegor.antiplagiacode.clazz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itegor.antiplagiacode.clazz.ClassEntity;
import ru.itegor.antiplagiacode.clazz.ClassMapper;
import ru.itegor.antiplagiacode.clazz.ClassRepository;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;
import ru.itegor.antiplagiacode.clazz.service.ClassService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassMapper classMapper;
    private final ClassRepository classRepository;

    @Override
    public List<ClassResponseDto> getAll() {
        List<ClassEntity> classEntities = classRepository.findAll();
        return classEntities.stream()
                .map(classMapper::toClassResponseDto)
                .toList();
    }

    @Override
    public ClassResponseDto getOne(Long id) {
        Optional<ClassEntity> classEntityOptional = classRepository.findById(id);
        return classMapper.toClassResponseDto(classEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @Override
    public List<ClassResponseDto> getMany(List<Long> ids) {
        List<ClassEntity> classEntities = classRepository.findAllById(ids);
        return classEntities.stream()
                .map(classMapper::toClassResponseDto)
                .toList();
    }

    @Override
    public ClassResponseDto create(MergeClassRequestDto dto) {
        ClassEntity classEntity = classMapper.toEntity(dto);
        ClassEntity resultClassEntity = classRepository.save(classEntity);
        return classMapper.toClassResponseDto(resultClassEntity);
    }

    @Override
    public ClassResponseDto delete(Long id) {
        ClassEntity classEntity = classRepository.findById(id).orElse(null);
        if (classEntity != null) {
            classRepository.delete(classEntity);
        }
        return classMapper.toClassResponseDto(classEntity);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        classRepository.deleteAllById(ids);
    }
}

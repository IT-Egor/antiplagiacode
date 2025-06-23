package ru.itegor.antiplagiacode.clazz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itegor.antiplagiacode.clazz.ClassEntity;
import ru.itegor.antiplagiacode.clazz.ClassMapper;
import ru.itegor.antiplagiacode.clazz.ClassRepository;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;
import ru.itegor.antiplagiacode.clazz.service.ClassService;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassMapper classMapper;
    private final ClassRepository classRepository;

    private final ObjectMapper objectMapper;

    @Override
    public List<ClassResponseDto> getAll() {
        List<ClassEntity> classes = classRepository.findAll();
        return classes.stream()
                .map(classMapper::toClassResponseDto)
                .toList();
    }

    @Override
    public ClassResponseDto getOne(Long id) {
        return classMapper.toClassResponseDto(findById(id));
    }

    @Override
    public List<ClassResponseDto> getMany(List<Long> ids) {
        List<ClassEntity> classes = classRepository.findAllById(ids);
        return classes.stream()
                .map(classMapper::toClassResponseDto)
                .toList();
    }

    @Override
    public ClassResponseDto create(MergeClassRequestDto dto) {
        ClassEntity clazz = classMapper.toEntity(dto);
        ClassEntity resultClass = classRepository.save(clazz);
        return classMapper.toClassResponseDto(resultClass);
    }

    @Override
    public ClassResponseDto patch(Long id, MergeClassRequestDto dto) {
        ClassEntity clazz = findById(id);

        classMapper.updateWithNull(dto, clazz);

        ClassEntity resultClass = classRepository.save(clazz);
        return classMapper.toClassResponseDto(resultClass);
    }

    @Override
    public ClassResponseDto delete(Long id) {
        ClassEntity clazz = classRepository.findById(id).orElse(null);
        if (clazz != null) {
            classRepository.delete(clazz);
        }
        return classMapper.toClassResponseDto(clazz);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        classRepository.deleteAllById(ids);
    }

    private ClassEntity findById(Long id) {
        return classRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Class with id `%s` not found".formatted(id)));
    }
}

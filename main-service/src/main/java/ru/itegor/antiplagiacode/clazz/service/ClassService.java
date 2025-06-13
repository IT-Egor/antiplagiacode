package ru.itegor.antiplagiacode.clazz.service;

import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;

import java.util.List;

public interface ClassService {
    List<ClassResponseDto> getAll();

    ClassResponseDto getOne(Long id);

    List<ClassResponseDto> getMany(List<Long> ids);

    ClassResponseDto create(MergeClassRequestDto dto);

    ClassResponseDto delete(Long id);

    void deleteMany(List<Long> ids);
}

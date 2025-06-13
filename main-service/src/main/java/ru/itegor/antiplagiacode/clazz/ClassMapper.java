package ru.itegor.antiplagiacode.clazz;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassMapper {
    ClassResponseDto toClassResponseDto(ClassEntity classEntity);

    ClassEntity toEntity(MergeClassRequestDto mergeClassRequestDto);
}
package ru.itegor.antiplagiacode.clazz;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassMapper {
    ClassResponseDto toClassResponseDto(ClassEntity classEntity);

    ClassEntity toEntity(MergeClassRequestDto mergeClassRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClassEntity updateWithNull(MergeClassRequestDto classUpdateDto, @MappingTarget ClassEntity classEntity);
}
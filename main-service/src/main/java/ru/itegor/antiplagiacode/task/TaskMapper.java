package ru.itegor.antiplagiacode.task;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.task.dto.MergeTaskRequestDto;
import ru.itegor.antiplagiacode.task.dto.TaskResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    @Mapping(source = "classId", target = "clazz.id")
    TaskEntity toEntity(MergeTaskRequestDto mergeTaskRequestDto);

    @Mapping(source = "clazz.id", target = "classId")
    TaskResponseDto toTaskResponseDto(TaskEntity taskEntity);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TaskEntity updateWithNull(MergeTaskRequestDto dto, @MappingTarget TaskEntity taskEntity);
}
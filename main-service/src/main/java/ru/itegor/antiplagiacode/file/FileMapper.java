package ru.itegor.antiplagiacode.file;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.file.dto.FileMetadataDto;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "studentId", source = "student.id")
    FileResponseDto toFileResponseDto(FileEntity fileEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FileEntity setFileMetadata(FileMetadataDto dto, @MappingTarget FileEntity fileEntity);
}
package ru.itegor.antiplagiacode.comparison_result;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComparisonResultMapper {
    @Mapping(source = "comparedFileId", target = "comparedFile.id")
    @Mapping(source = "originalFileId", target = "originalFile.id")
    ComparisonResultEntity toEntity(ComparisonResultResponseDto comparisonResultResponseDto);

    @InheritInverseConfiguration(name = "toEntity")
    ComparisonResultResponseDto toComparisonResultResponseDto(ComparisonResultEntity comparisonResultEntity);
}
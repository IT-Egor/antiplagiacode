package ru.itegor.antiplagiacode.comparison_result;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;
import ru.itegor.antiplagiacode.comparison_result.dto.CreateComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.dto.UpdateComparisonResultRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComparisonResultMapper {
    @Mapping(source = "comparedFileId", target = "comparedFile.id")
    @Mapping(source = "originalFileId", target = "originalFile.id")
    ComparisonResultEntity toEntity(CreateComparisonResultRequestDto createComparisonResultRequestDto);

    @Mapping(source = "comparedFileId", target = "comparedFile.id")
    @Mapping(source = "originalFileId", target = "originalFile.id")
    ComparisonResultEntity toEntity(ComparisonResultResponseDto comparisonResultResponseDto);

    @InheritInverseConfiguration(name = "toEntity")
    ComparisonResultResponseDto toComparisonResultResponseDto(ComparisonResultEntity comparisonResultEntity);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ComparisonResultEntity updateWithNull(UpdateComparisonResultRequestDto dto, @MappingTarget ComparisonResultEntity comparisonResultEntity);

    @Mapping(source = "comparedFileId", target = "comparedFile.id")
    @Mapping(source = "originalFileId", target = "originalFile.id")
    ComparisonResultEntity toEntity(UpdateComparisonResultRequestDto updateComparisonResultRequestDto);
}
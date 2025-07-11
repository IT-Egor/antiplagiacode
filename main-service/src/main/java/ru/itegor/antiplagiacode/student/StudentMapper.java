package ru.itegor.antiplagiacode.student;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {
    @Mapping(target = "classId", source = "studentClassEntity.clazz.id")
    @Mapping(target = "studentId", source = "studentClassEntity.student.id")
    StudentResponseDto toStudentResponseDto(StudentClassEntity studentClassEntity);
}
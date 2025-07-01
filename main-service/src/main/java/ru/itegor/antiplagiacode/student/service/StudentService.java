package ru.itegor.antiplagiacode.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itegor.antiplagiacode.student.StudentResponseDto;

import java.util.List;

public interface StudentService {
    Page<StudentResponseDto> getAll(Pageable pageable);

    StudentResponseDto getOneById(Long id);

    StudentResponseDto getOneByStudentIdAndClassId(Long studentId, Long classId);

    List<Long> getStudentsByClassId(Long classId);

    StudentResponseDto addStudentToClass(Long studentId, Long classId);

    List<StudentResponseDto> addStudentsToClass(List<Long> studentIds, Long classId);

    StudentResponseDto changeStudentClass(Long studentId, Long classId);

    int changeStudentsClasses(List<Long> studentIds, Long classId);

    int changeClassOfAllStudents(Long oldClassId, Long newClassId);

    StudentResponseDto delete(Long id);

    void deleteMany(List<Long> ids);
}

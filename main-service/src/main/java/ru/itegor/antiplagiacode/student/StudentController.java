package ru.itegor.antiplagiacode.student;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.student.service.StudentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public PagedModel<StudentResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<StudentResponseDto> studentResponseDtos = studentService.getAll(pageable);
        return new PagedModel<>(studentResponseDtos);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getOneById(@PathVariable Long id) {
        return studentService.getOneById(id);
    }

    @GetMapping("/by-student-id-and-class-id")
    public StudentResponseDto getOneByStudentIdAndClassId(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.getOneByStudentIdAndClassId(studentId, classId);
    }

    @GetMapping("/by-class-id")
    public List<Long> getStudentsByClassId(@RequestParam Long classId) {
        return studentService.getStudentsByClassId(classId);
    }

    @PostMapping
    public StudentResponseDto addStudentToClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.addStudentToClass(studentId, classId);
    }

    @PatchMapping
    public StudentResponseDto changeStudentClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.changeStudentClass(studentId, classId);
    }

    @PatchMapping("/patch-many")
    public int patchMany(@RequestParam List<Long> studentIds, @RequestParam Long classId) {
        return studentService.changeStudentsClasses(studentIds, classId);
    }

    @PatchMapping("/patch-all-class")
    public int patchAllStudentsInClass(@RequestParam Long oldClassId, @RequestParam Long newClassId) {
        return studentService.changeClassOfAllStudents(oldClassId, newClassId);
    }

    @DeleteMapping("/{id}")
    public StudentResponseDto delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        studentService.deleteMany(ids);
    }
}

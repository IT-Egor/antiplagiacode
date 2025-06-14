package ru.itegor.antiplagiacode.student;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.student.service.StudentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<StudentResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<StudentResponseDto> studentResponseDtos = studentService.getAll(pageable);
        return new PagedModel<>(studentResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto getOneById(@PathVariable Long id) {
        return studentService.getOneById(id);
    }

    @GetMapping("/by-student-id-and-class-id")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto getOneByStudentIdAndClassId(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.getOneByStudentIdAndClassId(studentId, classId);
    }

    @GetMapping("/by-class-id")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getStudentsByClassId(@RequestParam Long classId) {
        return studentService.getStudentsByClassId(classId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto addStudentToClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.addStudentToClass(studentId, classId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto changeStudentClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.changeStudentClass(studentId, classId);
    }

    @PatchMapping("/patch-many")
    @ResponseStatus(HttpStatus.OK)
    public int patchMany(@RequestParam List<Long> studentIds, @RequestParam Long classId) {
        return studentService.changeStudentsClasses(studentIds, classId);
    }

    @PatchMapping("/patch-all-class")
    @ResponseStatus(HttpStatus.OK)
    public int patchAllStudentsInClass(@RequestParam Long oldClassId, @RequestParam Long newClassId) {
        return studentService.changeClassOfAllStudents(oldClassId, newClassId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<Long> ids) {
        studentService.deleteMany(ids);
    }
}

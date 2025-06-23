package ru.itegor.antiplagiacode.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Student", description = "Student API. Links users with the `STUDENT` role to classes")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all students")
    public PagedModel<StudentResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<StudentResponseDto> studentResponseDtos = studentService.getAll(pageable);
        return new PagedModel<>(studentResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get one student by id")
    public StudentResponseDto getOneById(@PathVariable Long id) {
        return studentService.getOneById(id);
    }

    @GetMapping("/by-student-id-and-class-id")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get one student by student id and class id")
    public StudentResponseDto getOneByStudentIdAndClassId(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.getOneByStudentIdAndClassId(studentId, classId);
    }

    @GetMapping("/by-class-id")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all students by class id", description = "Get all students belonging to a class")
    public List<Long> getStudentsByClassId(@RequestParam Long classId) {
        return studentService.getStudentsByClassId(classId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add student to class")
    public StudentResponseDto addStudentToClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.addStudentToClass(studentId, classId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change student class")
    public StudentResponseDto changeStudentClass(@RequestParam Long studentId, @RequestParam Long classId) {
        return studentService.changeStudentClass(studentId, classId);
    }

    @PatchMapping("/patch-many")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change students classes", description = "Change the class of multiple students")
    public int patchMany(@RequestParam List<Long> studentIds, @RequestParam Long classId) {
        return studentService.changeStudentsClasses(studentIds, classId);
    }

    @PatchMapping("/patch-all-class")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change class of all students", description = "Change the class of all students in a class")
    public int patchAllStudentsInClass(@RequestParam Long oldClassId, @RequestParam Long newClassId) {
        return studentService.changeClassOfAllStudents(oldClassId, newClassId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete student")
    public StudentResponseDto delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete multiple students")
    public void deleteMany(@RequestParam List<Long> ids) {
        studentService.deleteMany(ids);
    }
}

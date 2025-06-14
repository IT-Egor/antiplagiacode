package ru.itegor.antiplagiacode.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.task.dto.MergeTaskRequestDto;
import ru.itegor.antiplagiacode.task.dto.TaskResponseDto;
import ru.itegor.antiplagiacode.task.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public PagedModel<TaskResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<TaskResponseDto> taskResponseDtos = taskService.getAll(pageable);
        return new PagedModel<>(taskResponseDtos);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getOne(@PathVariable Long id) {
        return taskService.getOne(id);
    }

    @GetMapping("/by-class-id")
    public List<TaskResponseDto> getTasksForClass(@RequestParam Long classId) {
        return taskService.getTasksForClass(classId);
    }

    @GetMapping("/by-class-id/relevant")
    public List<TaskResponseDto> getRelevantTasksForClass(@RequestParam Long classId) {
        return taskService.getRelevantTasksForClass(classId);
    }

    @GetMapping("/by-student-id/relevant")
    public List<TaskResponseDto> getRelevantTasksForStudent(@RequestParam Long studentId) {
        return taskService.getRelevantTasksForStudent(studentId);
    }

    @GetMapping("/by-class-id/irrelevant")
    public List<TaskResponseDto> getIrrelevantTasksForClass(@RequestParam Long classId) {
        return taskService.getIrrelevantTasksForClass(classId);
    }

    @GetMapping("/by-student-id/irrelevant")
    public List<TaskResponseDto> getIrrelevantTasksForStudent(@RequestParam Long studentId) {
        return taskService.getIrrelevantTasksForStudent(studentId);
    }

    @PostMapping
    public TaskResponseDto create(@RequestBody @Valid MergeTaskRequestDto dto) {
        return taskService.create(dto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDto patch(@PathVariable Long id, @RequestBody @Valid MergeTaskRequestDto dto) {
        return taskService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public TaskResponseDto delete(@PathVariable Long id) {
        return taskService.delete(id);
    }

    @DeleteMapping
    public int deleteForClass(@RequestParam Long classId) {
        return taskService.deleteForClass(classId);
    }
}

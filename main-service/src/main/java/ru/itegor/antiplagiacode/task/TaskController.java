package ru.itegor.antiplagiacode.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.task.dto.MergeTaskRequestDto;
import ru.itegor.antiplagiacode.task.dto.TaskResponseDto;
import ru.itegor.antiplagiacode.task.service.TaskService;
import ru.itegor.antiplagiacode.validation_groups.CreateValidation;
import ru.itegor.antiplagiacode.validation_groups.UpdateValidation;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@Tag(name = "Task", description = "Task API")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks")
    public PagedModel<TaskResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<TaskResponseDto> taskResponseDtos = taskService.getAll(pageable);
        return new PagedModel<>(taskResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get task by id")
    public TaskResponseDto getOne(@PathVariable Long id) {
        return taskService.getOne(id);
    }

    @GetMapping("/by-class-id")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tasks by class id", description = "Get all tasks for class")
    public List<TaskResponseDto> getTasksForClass(@RequestParam Long classId) {
        return taskService.getTasksForClass(classId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-class-id/relevant")
    @Operation(summary = "Get relevant tasks by class id", description = "Get relevant tasks for the class. A task is considered relevant if its start date has passed and its end date has not yet passed")
    public List<TaskResponseDto> getRelevantTasksForClass(@RequestParam Long classId) {
        return taskService.getRelevantTasksForClass(classId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-student-id/relevant")
    @Operation(summary = "Get relevant tasks by student id", description = "Get relevant tasks for the student. A task is considered relevant if its start date has passed and its end date has not yet passed")
    public List<TaskResponseDto> getRelevantTasksForStudent(@RequestParam Long studentId) {
        return taskService.getRelevantTasksForStudent(studentId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-class-id/irrelevant")
    @Operation(summary = "Get irrelevant tasks by class id", description = "Get irrelevant tasks for the class. A task is considered irrelevant if its start date has not passed or its end date has passed")
    public List<TaskResponseDto> getIrrelevantTasksForClass(@RequestParam Long classId) {
        return taskService.getIrrelevantTasksForClass(classId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-student-id/irrelevant")
    @Operation(summary = "Get irrelevant tasks by student id", description = "Get irrelevant tasks for the student. A task is considered irrelevant if its start date has not passed or its end date has passed")
    public List<TaskResponseDto> getIrrelevantTasksForStudent(@RequestParam Long studentId) {
        return taskService.getIrrelevantTasksForStudent(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create task")
    public TaskResponseDto create(@RequestBody
                                  @Validated(CreateValidation.class)
                                  MergeTaskRequestDto dto) {
        return taskService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update task by id")
    public TaskResponseDto patch(@PathVariable Long id,
                                 @RequestBody
                                 @Validated(UpdateValidation.class)
                                 MergeTaskRequestDto dto) {
        return taskService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete task by id")
    public TaskResponseDto delete(@PathVariable Long id) {
        return taskService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete tasks for class", description = "Delete all tasks related to the class")
    public int deleteForClass(@RequestParam Long classId) {
        return taskService.deleteForClass(classId);
    }
}

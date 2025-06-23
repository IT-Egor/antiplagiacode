package ru.itegor.antiplagiacode.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itegor.antiplagiacode.clazz.ClassEntity;
import ru.itegor.antiplagiacode.clazz.ClassRepository;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.file.FileRepository;
import ru.itegor.antiplagiacode.task.TaskEntity;
import ru.itegor.antiplagiacode.task.TaskMapper;
import ru.itegor.antiplagiacode.task.TaskRepository;
import ru.itegor.antiplagiacode.task.dto.MergeTaskRequestDto;
import ru.itegor.antiplagiacode.task.dto.TaskResponseDto;
import ru.itegor.antiplagiacode.task.service.TaskService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final ClassRepository classRepository;
    private final FileRepository fileRepository;

    @Override
    public Page<TaskResponseDto> getAll(Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findAll(pageable);
        return tasks.map(taskMapper::toTaskResponseDto);
    }

    @Override
    public TaskResponseDto getOne(Long id) {
        return taskMapper.toTaskResponseDto(findById(id));
    }

    @Override
    public List<TaskResponseDto> getTasksForClass(Long classId) {
        ClassEntity clazz = findClassById(classId);
        return taskRepository.findAllByClazz_Id(clazz.getId()).stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getRelevantTasksForClass(Long classId) {
        return taskRepository.findRelevantForClass(classId, LocalDate.now()).stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getIrrelevantTasksForClass(Long classId) {
        return taskRepository.findIrrelevantForClass(classId,LocalDate.now()).stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto create(MergeTaskRequestDto dto) {
        TaskEntity task = taskMapper.toEntity(dto);
        findClassById(dto.getClassId());
        TaskEntity resultTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(resultTask);
    }

    @Override
    public TaskResponseDto patch(Long id, MergeTaskRequestDto dto) {
        TaskEntity task = findById(id);
        if (dto.getClassId() != null) {
            findClassById(dto.getClassId());
        }

        taskMapper.updateWithNull(dto, task);

        TaskEntity resultTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(resultTask);
    }

    @Override
    public TaskResponseDto delete(Long id) {
        TaskEntity task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
        }
        return taskMapper.toTaskResponseDto(task);
    }

    @Override
    public int deleteForClass(Long classId) {
        return taskRepository.deleteAllByClazz_Id(classId);
    }

    private TaskEntity findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Task with id `%s` not found".formatted(id)));
    }

    private ClassEntity findClassById(Long id) {
        return classRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Class with id `%s` not found".formatted(id)));
    }
}
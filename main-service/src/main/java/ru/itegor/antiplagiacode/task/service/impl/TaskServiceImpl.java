package ru.itegor.antiplagiacode.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itegor.antiplagiacode.clazz.ClassEntity;
import ru.itegor.antiplagiacode.clazz.ClassRepository;
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

    @Override
    public Page<TaskResponseDto> getAll(Pageable pageable) {
        Page<TaskEntity> taskEntities = taskRepository.findAll(pageable);
        return taskEntities.map(taskMapper::toTaskResponseDto);
    }

    @Override
    public TaskResponseDto getOne(Long id) {
        TaskEntity taskEntityOptional = findById(id);
        return taskMapper.toTaskResponseDto(taskEntityOptional);
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
    public List<TaskResponseDto> getRelevantTasksForStudent(Long studentId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<TaskResponseDto> getIrrelevantTasksForClass(Long classId) {
        return taskRepository.findIrrelevantForClass(classId,LocalDate.now()).stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getIrrelevantTasksForStudent(Long studentId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public TaskResponseDto create(MergeTaskRequestDto dto) {
        TaskEntity taskEntity = taskMapper.toEntity(dto);
        findClassById(dto.getClassId());
        TaskEntity resultTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toTaskResponseDto(resultTaskEntity);
    }

    @Override
    public TaskResponseDto patch(Long id, MergeTaskRequestDto dto) {
        TaskEntity taskEntity = findById(id);
        if (dto.getClassId() != null) {
            findClassById(dto.getClassId());
        }

        taskMapper.updateWithNull(dto, taskEntity);

        TaskEntity resultTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toTaskResponseDto(resultTaskEntity);
    }

    @Override
    public TaskResponseDto delete(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElse(null);
        if (taskEntity != null) {
            taskRepository.delete(taskEntity);
        }
        return taskMapper.toTaskResponseDto(taskEntity);
    }

    @Override
    public int deleteForClass(Long classId) {
        return taskRepository.deleteAllByClazz_Id(classId);
    }

    private TaskEntity findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    private ClassEntity findClassById(Long id) {
        return classRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Class with id `%s` not found".formatted(id)));
    }
}

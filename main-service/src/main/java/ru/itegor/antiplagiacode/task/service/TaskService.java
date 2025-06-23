package ru.itegor.antiplagiacode.task.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itegor.antiplagiacode.task.dto.MergeTaskRequestDto;
import ru.itegor.antiplagiacode.task.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    Page<TaskResponseDto> getAll(Pageable pageable);

    TaskResponseDto getOne(Long id);

    List<TaskResponseDto> getTasksForClass(Long classId);

    List<TaskResponseDto> getRelevantTasksForClass(Long classId);

    List<TaskResponseDto> getIrrelevantTasksForClass(Long classId);

    TaskResponseDto create(MergeTaskRequestDto dto);

    TaskResponseDto patch(Long id, MergeTaskRequestDto dto);

    TaskResponseDto delete(Long id);

    int deleteForClass(Long classId);
}

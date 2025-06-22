package ru.itegor.antiplagiacode.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.file.FileEntity;
import ru.itegor.antiplagiacode.file.FileMapper;
import ru.itegor.antiplagiacode.file.FileRepository;
import ru.itegor.antiplagiacode.file.dto.FileMetadataDto;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;
import ru.itegor.antiplagiacode.file.service.FileService;
import ru.itegor.antiplagiacode.storage.S3Client;
import ru.itegor.antiplagiacode.student.StudentRepository;
import ru.itegor.antiplagiacode.task.TaskEntity;
import ru.itegor.antiplagiacode.task.TaskRepository;
import ru.itegor.antiplagiacode.user.UserEntity;
import ru.itegor.antiplagiacode.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;
    private final S3Client s3Client;

    @Override
    public Page<FileResponseDto> getAll(Pageable pageable) {
        Page<FileEntity> files = fileRepository.findAll(pageable);
        return files.map(fileMapper::toFileResponseDto);
    }

    @Override
    public FileResponseDto getOne(Long id) {
        return fileMapper.toFileResponseDto(findById(id));
    }

    @Override
    public FileResponseDto upload(Long studentId, Long taskId, MultipartFile file) {
        UserEntity student = findStudentById(studentId);
        TaskEntity task = findTaskById(taskId);
        checkStudentInTask(task, student);

        FileEntity fileEntity = findFileByStudentIdAndTaskId(studentId, taskId);
        fileEntity.setStudent(student);
        fileEntity.setTask(task);

        String prefix = String.format("task-id/%d/student-id/%d/", taskId, studentId);
        String objectName = prefix + file.getOriginalFilename();

        fileMapper.setFileMetadata(createFileMetadataDto(objectName, file.getSize()), fileEntity);
        FileResponseDto response = fileMapper.toFileResponseDto(fileRepository.save(fileEntity));

        List<String> objects = s3Client.listObjects(prefix, false);
        if (!objects.isEmpty()) {
            s3Client.deleteObjects(objects);
        }
        s3Client.uploadFile(objectName, file);

        return response;
    }

    @Override
    public byte[] download(Long storageId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<String> getFileLines(Long storageId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public FileResponseDto delete(Long id) {
        FileEntity fileEntity = fileRepository.findById(id).orElse(null);
        if (fileEntity != null) {
            fileRepository.delete(fileEntity);
        }
        return fileMapper.toFileResponseDto(fileEntity);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        fileRepository.deleteAllById(ids);
    }

    private FileEntity findById(Long id) {
        return fileRepository.findById(id).orElseThrow(() ->
                new NotFoundException("File with id `%s` not found".formatted(id)));
    }

    private FileEntity findFileByStudentIdAndTaskId(Long studentId, Long taskId) {
        return fileRepository.findByStudent_IdAndTask_Id(studentId, taskId).orElse(new FileEntity());
    }

    private UserEntity findStudentById(Long studentId) {
        return userRepository.findById(studentId).orElseThrow(() ->
                new NotFoundException("Student with id `%s` not found".formatted(studentId)));
    }

    private void checkStudentInTask(TaskEntity task, UserEntity student) {
        if (!studentRepository.existsByStudent_IdAndClazz_Id(student.getId(), task.getClazz().getId())) {
            throw new NotFoundException("Student with id `%s` doesn't have task with id `%s`".formatted(student.getId(), task.getId()));
        }
    }

    private TaskEntity findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Task with id `%s` not found".formatted(id)));
    }

    private FileMetadataDto createFileMetadataDto(String objectName, Long fileSizeByte) {
        return new FileMetadataDto(
                objectName,
                LocalDateTime.now(),
                fileSizeByte
        );
    }
}

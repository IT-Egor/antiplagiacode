package ru.itegor.antiplagiacode.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.kafka.producer.ComparisonResultProducer;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.file.FileEntity;
import ru.itegor.antiplagiacode.file.FileMapper;
import ru.itegor.antiplagiacode.file.FileRepository;
import ru.itegor.antiplagiacode.file.dto.FileMetadataDto;
import ru.itegor.antiplagiacode.file.dto.FileMetadataResponseDto;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;
import ru.itegor.antiplagiacode.file.service.FileService;
import ru.itegor.antiplagiacode.kafka.message.ComparisonResultMessage;
import ru.itegor.antiplagiacode.storage.service.S3Service;
import ru.itegor.antiplagiacode.student.StudentRepository;
import ru.itegor.antiplagiacode.task.TaskEntity;
import ru.itegor.antiplagiacode.task.TaskRepository;
import ru.itegor.antiplagiacode.user.UserEntity;
import ru.itegor.antiplagiacode.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;
    private final S3Service s3Service;
    private final ComparisonResultProducer producer;

    @Override
    public Page<FileMetadataResponseDto> getAll(Pageable pageable) {
        Page<FileEntity> files = fileRepository.findAll(pageable);
        return files.map(fileMapper::toFileMetadataResponseDto);
    }

    @Override
    public FileMetadataResponseDto getOne(Long id) {
        return fileMapper.toFileMetadataResponseDto(findById(id));
    }

    @Override
    public FileMetadataResponseDto upload(Long studentId, Long taskId, MultipartFile file) {
        UserEntity student = findStudentById(studentId);
        TaskEntity task = findTaskById(taskId);
        checkStudentInTask(task, student);

        String objectName = s3Service.createObjectName(taskId, studentId, file.getOriginalFilename());

        FileEntity fileEntity = findFileByStudentIdAndTaskId(studentId, taskId);
        fileEntity.setStudent(student);
        fileEntity.setTask(task);
        fileMapper.setFileMetadata(createFileMetadataDto(objectName, file.getSize()), fileEntity);

        FileMetadataResponseDto response = fileMapper.toFileMetadataResponseDto(fileRepository.save(fileEntity));

        s3Service.uploadFile(objectName, file);

        sendFileComparisonRequest(fileEntity);

        return response;
    }

    @Override
    public FileResponseDto download(Long id) {
        FileEntity fileEntity = findById(id);
        String objectName = fileEntity.getObjectName();
        String fileName = s3Service.extractFileName(objectName);
        return new FileResponseDto(
                fileName,
                s3Service.downloadFile(objectName));
    }

    @Override
    public FileMetadataResponseDto delete(Long id) {
        FileEntity fileEntity = fileRepository.findById(id).orElse(null);
        if (fileEntity != null) {
            fileRepository.delete(fileEntity);
        }
        FileMetadataResponseDto response = fileMapper.toFileMetadataResponseDto(fileEntity);
        s3Service.deleteFile(response.getObjectName());
        return response;
    }

    @Override
    public void deleteMany(List<Long> ids) {
        List<FileEntity> fileEntities = fileRepository.findAllById(ids);
        fileRepository.deleteAllById(ids);
        s3Service.deleteFiles(fileEntities.stream().map(FileEntity::getObjectName).toList());
    }

    private void sendFileComparisonRequest(FileEntity fileEntity) {
        Long originalFileId = fileEntity.getId();
        List<Long> comparedFileIds = findFilesByTaskId(fileEntity.getTask().getId()).stream()
                .map(FileEntity::getId)
                .collect(Collectors.toList());
        comparedFileIds.remove(originalFileId);

        producer.sendComparisonResult(
                ComparisonResultMessage.builder()
                        .originalFileId(fileEntity.getId())
                        .comparedFileIds(comparedFileIds)
                        .build()
        );
    }

    private FileEntity findById(Long id) {
        return fileRepository.findById(id).orElseThrow(() ->
                new NotFoundException("File with id `%s` not found".formatted(id)));
    }

    private FileEntity findFileByStudentIdAndTaskId(Long studentId, Long taskId) {
        return fileRepository.findByStudent_IdAndTask_Id(studentId, taskId).orElse(new FileEntity());
    }

    private List<FileEntity> findFilesByTaskId(Long taskId) {
        return fileRepository.findAllByTask_Id(taskId);
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

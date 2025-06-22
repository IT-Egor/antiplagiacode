package ru.itegor.antiplagiacode.file.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;

import java.util.List;

public interface FileService {
    Page<FileResponseDto> getAll(Pageable pageable);

    FileResponseDto getOne(Long id);

    FileResponseDto upload(Long studentId, Long taskId, MultipartFile file);

    byte[] download(Long storageId);

    List<String> getFileLines(Long storageId);

    FileResponseDto delete(Long id);

    void deleteMany(List<Long> ids);
}

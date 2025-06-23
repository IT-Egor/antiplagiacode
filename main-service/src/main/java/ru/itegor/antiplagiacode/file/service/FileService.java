package ru.itegor.antiplagiacode.file.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.file.dto.FileMetadataResponseDto;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;

import java.util.List;

public interface FileService {
    Page<FileMetadataResponseDto> getAll(Pageable pageable);

    FileMetadataResponseDto getOne(Long id);

    FileMetadataResponseDto upload(Long studentId, Long taskId, MultipartFile file);

    FileResponseDto download(Long id);

    FileMetadataResponseDto delete(Long id);

    void deleteMany(List<Long> ids);
}

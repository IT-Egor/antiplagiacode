package ru.itegor.antiplagiacode.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    String createPrefix(Long taskId, Long studentId);

    String createObjectName(Long taskId, Long studentId, String originalFilename);

    String extractPrefix(String objectName);

    String extractFileName(String objectName);

    void uploadFile(String objectName, MultipartFile file);

    byte[] downloadFile(String objectName);

    void deleteFile(String objectName);

    void deleteFiles(List<String> objectNames);
}

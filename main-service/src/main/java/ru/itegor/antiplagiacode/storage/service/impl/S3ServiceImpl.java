package ru.itegor.antiplagiacode.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.storage.client.S3Client;
import ru.itegor.antiplagiacode.storage.service.S3Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;
    private final String prefixTemplate = "task-id/%d/student-id/%d/";

    @Override
    public String createPrefix(Long taskId, Long studentId) {
        return String.format(prefixTemplate, taskId, studentId);
    }

    @Override
    public String createObjectName(Long taskId, Long studentId, String fileName) {
        return createPrefix(taskId, studentId) + fileName;
    }

    @Override
    public void uploadFile(String objectName, MultipartFile file) {
        List<String> objects = s3Client.listObjects(extractPrefix(objectName), false);
        if (!objects.isEmpty()) {
            deleteFiles(objects);
        }
        s3Client.uploadFile(objectName, file);
    }

    @Override
    public byte[] downloadFile(String objectName) {
        return s3Client.downloadFile(objectName);
    }

    @Override
    public void deleteFile(String objectName) {
        s3Client.deleteObject(objectName);
    }

    @Override
    public void deleteFiles(List<String> objectNames) {
        s3Client.deleteObjects(objectNames);
    }

    @Override
    public String extractPrefix(String objectName) {
        return objectName.substring(0, objectName.lastIndexOf('/') + 1);
    }

    @Override
    public String extractFileName(String objectName) {
        return objectName.substring(objectName.lastIndexOf('/') + 1);
    }
}

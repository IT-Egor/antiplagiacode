package ru.itegor.antiplagiacode.storage.client;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Client {
    void uploadFile(String objectName, MultipartFile file);

    byte[] downloadFile(String objectName);

    List<String> listObjects(String prefix, boolean recursive);

    void deleteObject(String objectName);

    void deleteObjects(List<String> objectNames);

    boolean isObjectExists(String objectName);
}

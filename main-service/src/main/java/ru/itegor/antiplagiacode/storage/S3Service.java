package ru.itegor.antiplagiacode.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    String uploadFile(String prefix, MultipartFile file);

    byte[] downloadFile(String objectName);

    List<String> listObjects(String prefix, boolean recursive);

    void updateObject(String objectName, MultipartFile file);

    void deleteObject(String objectName);

    void deleteObjects(List<String> objectNames);

    boolean isObjectExists(String objectName);
}

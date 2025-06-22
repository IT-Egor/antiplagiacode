package ru.itegor.antiplagiacode.storage.minio;

import io.micrometer.common.util.StringUtils;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.exception.exceptions.FileStorageException;
import ru.itegor.antiplagiacode.storage.S3Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MinioStorageService implements S3Service {
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageService(MinioClient minioClient,
                               @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(String prefix, MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = StringUtils.isNotBlank(prefix)
                    ? prefix + "/" + fileName
                    : fileName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return objectName;
        } catch (Exception e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    @Override
    public byte[] downloadFile(String objectName) {
        try {
            GetObjectResponse object = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return object.readAllBytes();
        } catch (Exception e) {
            throw new FileStorageException("Failed to download file", e);
        }
    }

    @Override
    public List<String> listObjects(String prefix, boolean recursive) {
        try {
            List<String> objectNames = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(recursive)
                            .build());

            for (Result<Item> result : results) {
                objectNames.add(result.get().objectName());
            }
            return objectNames;
        } catch (Exception e) {
            throw new FileStorageException("Failed to list objects", e);
        }
    }

    @Override
    public void updateObject(String objectName, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Failed to update object", e);
        }
    }

    @Override
    public void deleteObject(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Failed to delete object", e);
        }
    }

    @Override
    public void deleteObjects(List<String> objectNames) {
        try {
            List<DeleteObject> objects = objectNames.stream()
                    .map(DeleteObject::new)
                    .collect(Collectors.toList());

            minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Failed to delete objects", e);
        }
    }

    @Override
    public boolean isObjectExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String extractFileName(String objectName) {
        int lastSlashIndex = objectName.lastIndexOf('/');
        return objectName.substring(
                lastSlashIndex + 1,
                objectName.indexOf("-", lastSlashIndex));
    }

    private static String generateFileName(String originalName) {
        return originalName + "-" + UUID.randomUUID();
    }
}

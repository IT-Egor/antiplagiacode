package ru.itegor.antiplagiacode.storage.minio;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.exception.exceptions.FileStorageException;
import ru.itegor.antiplagiacode.storage.S3Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MinioStorageClient implements S3Client {
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageClient(MinioClient minioClient,
                              @Value("${minio.bucket-name}") String bucketName,
                              @Value("${minio.access-key}") String accessKey) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        initializeBucket(bucketName, accessKey);
    }

    @Override
    public void uploadFile(String objectName, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
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
    public List<String> deleteObjects(List<String> objectNames) {
        try {
            List<DeleteObject> objects = objectNames.stream()
                    .map(DeleteObject::new)
                    .collect(Collectors.toList());


            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build());

            List<String> deletedObjects = new ArrayList<>();
            for (Result<DeleteError> result : results) {
                deletedObjects.add(result.get().objectName());
            }
            return deletedObjects;
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

    public String extractFileName(String objectName) {
        return objectName.substring(objectName.lastIndexOf('/') + 1);
    }

    private void initializeBucket(String bucketName, String accessKey) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }

            setBucketPolicy(bucketName, accessKey);
        } catch (Exception e) {
            throw new FileStorageException("Failed to create bucket", e);
        }
    }

    @SuppressWarnings("all")
    private void setBucketPolicy(String bucketName, String accessKey) {
        try {
            String policyJson = """
            {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Deny",
                        "Principal": "*",
                        "Action": "s3:*",
                        "Resource": [
                            "arn:aws:s3:::%s",
                            "arn:aws:s3:::%s/*"
                        ],
                        "Condition": {
                            "StringNotEquals": {
                                "aws:username": "%s"
                            }
                        }
                    }
                ]
            }
            """.formatted(bucketName, bucketName, accessKey);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policyJson)
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Failed to set bucket policy", e);
        }
    }
}

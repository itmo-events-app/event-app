package org.itmo.eventapp.main.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.itmo.eventapp.main.model.dto.response.FileDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MinioService {
    private final MinioClient minioClient;

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .stream(multipartFile.getInputStream(), multipartFile.getInputStream().available(), -1)
                .bucket(bucketName)
                .object(multipartFile.getOriginalFilename())
                .build());
    }

    @SneakyThrows
    public void uploadWithModifiedFileName(MultipartFile multipartFile, String bucketName, String fileName) {
        if (multipartFile == null) return;
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .stream(multipartFile.getInputStream(), multipartFile.getInputStream().available(), -1)
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    public void delete(String bucket, String object) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(object)
                        .build()
        );
    }

    @SneakyThrows
    public void deleteImageByPrefix(String bucket, String prefix) {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            return;
        }
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).build());
        Iterator<Result<Item>> iterator = results.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(item.objectName()).build());
        }
    }

    @SneakyThrows
    public List<String> getFileNamesByPrefix(String bucket, String prefix) {
        List<String> filenames = new ArrayList<>();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            return filenames;
        }
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).build());
            for (Result<Item> result : results) {
                filenames.add(result.get().objectName());
            }
        } catch (Exception e) {
            throw new MinioException("Error getting filenames: " + e.getMessage());
        }
        return filenames;
    }


    @SneakyThrows
    public List<FileDataResponse> getFileDataByPrefix(String bucket, String prefix) {
        List<FileDataResponse> filenames = new ArrayList<>();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            return filenames;
        }
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).build());
            for (Result<Item> result : results) {
                String filename = result.get().objectName();
                String presignedUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucket)
                                .object(filename)
                                .expiry(60 * 60 * 24)
                                .build());
                filenames.add(new FileDataResponse(filename, presignedUrl));
            }
        } catch (Exception e) {
            throw new MinioException("Error getting filenames: " + e.getMessage());
        }
        return filenames;
    }

    @SneakyThrows
    public void copyImagesWithPrefix(String sourceBucket, String destinationBucket, String sourcePrefix, String destinationPrefix) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(sourceBucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(sourceBucket).build());
            }

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(destinationBucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(destinationBucket).build());
            }
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(sourceBucket).prefix(sourcePrefix).build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                String sourceObjectName = item.objectName();
                String destinationObjectName = destinationPrefix + sourceObjectName.substring(sourcePrefix.length());
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .source(CopySource.builder().bucket(sourceBucket).object(sourceObjectName).build())
                                .bucket(destinationBucket)
                                .object(destinationObjectName)
                                .build()
                );
            }
        } catch (Exception ex) {
            throw new MinioException("Error copying images: " + ex.getMessage());
        }
    }
}

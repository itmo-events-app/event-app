package org.itmo.eventapp.main.minio;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.Iterator;

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
    public void deleteImageByEvent(String bucket,String eventId){
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            return;
        }
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(eventId).build());
        Iterator<Result<Item>> iterator = results.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(item.objectName()).build());
        }
    }
}

package com.greenblat.cloudfilestorage.repository.impl;

import com.greenblat.cloudfilestorage.config.minio.MinioProperties;
import com.greenblat.cloudfilestorage.exception.MinioOperationException;
import com.greenblat.cloudfilestorage.repository.MinioRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Repository
@RequiredArgsConstructor
public class MinioRepositoryImpl implements MinioRepository {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public void saveFile(String filename, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(minioProperties.getBucket())
                            .object(filename)
                            .build()
            );
            inputStream.close();
        } catch (Exception e) {
            throw new MinioOperationException("Image upload failed" + e.getMessage());
        }
    }

    @Override
    public void createBucket() {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        } catch (ServerException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException e) {
            throw new MinioOperationException("Create bucket failed: " + e.getMessage());
        }
    }

    @Override
    public boolean existsBucket() {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new MinioOperationException("Exists bucket failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(filename)
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new MinioOperationException("Delete file failed: " + e.getMessage());
        }
    }

    @Override
    public void copyFile(String filename, String destination) {
        var copySource = CopySource.builder()
                .bucket(minioProperties.getBucket())
                .object(filename)
                .build();
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(destination)
                            .source(copySource)
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new MinioOperationException("Copy file failed: " + e.getMessage());
        }
    }
}

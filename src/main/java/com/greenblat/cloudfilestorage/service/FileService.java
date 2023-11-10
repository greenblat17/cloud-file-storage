package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.config.minio.MinioProperties;
import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.exception.FileUploadException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(FileRequest request) {
        createBucket();
        var file = request.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException("File must have the name");
        }
        var fileName = generateFileName(file);
        saveFile(file, fileName);
        return fileName;
    }

    @SneakyThrows
    private void saveFile(MultipartFile file, String fileName) {
        var inputStream = extractInputStream(file);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .build()
        );
    }

    private InputStream extractInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new FileUploadException("File upload failed: " + e.getMessage());
        }
    }

    @SneakyThrows
    private void createBucket() {
        var found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() +  "." + getExtension(file);
    }

    private String getExtension(MultipartFile file) {
        return getExtensionFromFileName(file.getOriginalFilename());
    }

    public String getExtensionFromFileName(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}

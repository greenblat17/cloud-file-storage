package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.config.minio.MinioProperties;
import com.greenblat.cloudfilestorage.dto.FolderRequest;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @SneakyThrows
    public void createFolder(FolderRequest request) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(getFolderPath(request.parentFolderPath(), request.newFolder()))
                        .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                        .build()
        );
    }

    private String getFolderPath(String parentFolder, String newFolder) {
        return parentFolder + newFolder + "/";
    }

}

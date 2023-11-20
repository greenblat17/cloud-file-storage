package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.dto.FolderUploadRequest;
import com.greenblat.cloudfilestorage.exception.FileUploadException;
import com.greenblat.cloudfilestorage.repository.MinioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioRepository minioRepository;


    public void upload(FileRequest request, String path) {
        createBucket();

        var file = request.getFile();
        checkEmptyFile(file);

        var fileName = path + "/" + generateFileName(file);
        var inputStream = extractInputStream(file);

        minioRepository.saveFile(fileName, inputStream);
    }

    public void uploadFiles(FolderUploadRequest filesRequest) {
        createBucket();

        var files = filesRequest.files();
        if (files == null || files.isEmpty()) {
            throw new FileUploadException("Files are empty");
        }

        for (MultipartFile file : files) {
            var fullFilename = generateFullFileName(filesRequest.parentPath(), generateFileName(file));
            var inputStream = extractInputStream(file);
            minioRepository.saveFile(fullFilename, inputStream);
        }
    }

    private void checkEmptyFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException("File must have the name");
        }
    }


    private InputStream extractInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new FileUploadException("File upload failed: " + e.getMessage());
        }
    }

    private void createBucket() {
        var found = minioRepository.existsBucket();
        if (!found) {
            minioRepository.createBucket();
        }
    }


    private String generateFullFileName(String path, String fileName) {
        return path + "/" + fileName;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() +  "." + getExtension(file);
    }

    private String getExtension(MultipartFile file) {
        return getExtensionFromFileName(Objects.requireNonNull(file.getOriginalFilename()));
    }

    public String getExtensionFromFileName(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}

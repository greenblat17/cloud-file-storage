package com.greenblat.cloudfilestorage.repository;

import com.greenblat.cloudfilestorage.dto.PathResponse;

import java.io.InputStream;
import java.util.List;

public interface MinioRepository {

    void saveFile(String filename, InputStream inputStream);

    void createBucket();

    boolean existsBucket();

    void deleteFile(String filename);

    void copyFile(String filename, String destination);

    List<PathResponse> findAllFilesAndFolders();
}
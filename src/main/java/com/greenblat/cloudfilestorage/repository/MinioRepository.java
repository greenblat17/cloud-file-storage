package com.greenblat.cloudfilestorage.repository;

import java.io.InputStream;

public interface MinioRepository {

    void saveFile(String filename, InputStream inputStream);

    void createBucket();

    boolean existsBucket();

    void deleteFile(String filename);

    void copyFile(String filename, String destination);

}

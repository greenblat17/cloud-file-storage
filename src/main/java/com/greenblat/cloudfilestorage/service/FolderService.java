package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.config.minio.MinioProperties;
import com.greenblat.cloudfilestorage.dto.FolderCreateRequest;
import com.greenblat.cloudfilestorage.dto.PathResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @SneakyThrows
    public Map<PathResponse, String> getAllFoldersAndFiles(String path) {
        var breadcrumbs = new String[]{};
        if (!path.isEmpty()) {
            breadcrumbs = path.split("/");
        }

        var buckets = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .recursive(true)
                        .build()
        );

        Map<PathResponse, String> paths = new HashMap();
        for (Result<Item> bucket : buckets) {
            var objectPath = bucket.get().objectName().split("/");

            if (objectPath.length < breadcrumbs.length) {
                continue;
            }

            for (int i = 0; i < objectPath.length; i++) {
                if (i == breadcrumbs.length) {
                    var pathResponse = new PathResponse(objectPath[i], isDirectory(objectPath[i]));
                    paths.put(pathResponse, encodeUrl(breadcrumbs, objectPath[i]));
                    break;
                } else if (!objectPath[i].equals(breadcrumbs[i])) {
                    break;
                }
            }
        }
        return paths;
    }

    private boolean isDirectory(String name) {
        return name.lastIndexOf(".") == -1;
    }

    @SneakyThrows
    public void createFolder(FolderCreateRequest request) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(getFolderPath(request.parentPath(), request.folderName()))
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build()
        );
    }

    public String[] getCurrentUrl(String path) {
        return path.split("/");
    }

    public Map<String, String> getUrlForPath(String path) {
        Map<String, String> urls = new HashMap<>();
        if (path.isEmpty()) {
            urls.put("/", "/");
        }
        var breadcrumb = path.split("/");
        StringBuilder currentPath = new StringBuilder();
        for (String s : breadcrumb) {
            currentPath.append(s);
            urls.put(s, currentPath.toString());
            currentPath.append("/");
        }
        return urls;
    }

    private String getFolderPath(String parentFolder, String newFolder) {
        return parentFolder + "/" + newFolder + "/";
    }

    private String encodeUrl(String[] breadcrumbs, String currentUrl) {
        StringBuilder sb = new StringBuilder();
        for (String breadcrumb : breadcrumbs) {
            sb.append(breadcrumb).append("/");
        }
        sb.append(currentUrl);
        return URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
    }

    private String decodeUrl(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

}

package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.dto.PathResponse;
import com.greenblat.cloudfilestorage.repository.MinioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MinioRepository minioRepository;

    public Map<PathResponse, String> search(String query) {
        var paths = minioRepository.findAllFilesAndFolders();

        var foundFiles = getAllFilesWithName(query, paths);
        var foundFolders = getAllFoldersWithName(query, paths);

        Map<PathResponse, String> result = new HashMap<>();
        result.putAll(foundFiles);
        result.putAll(foundFolders);

        return result;
    }

    private Map<PathResponse, String> getAllFilesWithName(String name, List<PathResponse> paths) {
        Map<PathResponse, String> files = new HashMap<>();
        for (PathResponse path : paths) {
            if (path.name().endsWith("/")) {
                continue;
            }

            var fullPath = path.name().split("/");
            var filename = getFileName(fullPath[fullPath.length - 1]);
            if (!path.isDir() && filename.equals(name)) {
                var pathResponse = new PathResponse(filename, false);
                files.put(pathResponse, getEncodedUrl(fullPath, fullPath.length - 1));
            }
        }
        return files;
    }

    private Map<PathResponse, String> getAllFoldersWithName(String name, List<PathResponse> paths) {
        Map<PathResponse, String> folders = new HashMap<>();
        for (PathResponse path : paths) {
            var parts = path.name().split("/");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals(name)) {
                    var pathResponse = new PathResponse(name, true);
                    folders.put(pathResponse, getEncodedUrl(parts, i + 1));
                    break;
                }
            }
        }
        return folders;
    }

    private String getEncodedUrl(String[] fullPath, int lastIndex) {
        StringBuilder url = new StringBuilder();
        for (int i = 0; i < lastIndex; i++) {
            url.append(fullPath[i]).append("/");
        }
        return URLEncoder.encode(url.toString(), StandardCharsets.UTF_8);
    }

    private String getFileName(String fullFileName) {
        return fullFileName.substring(0, fullFileName.lastIndexOf("."));
    }

    public String getExtensionFromFileName(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}

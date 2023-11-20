package com.greenblat.cloudfilestorage.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FolderUploadRequest(String parentPath,
                                  List<MultipartFile> files) {
}

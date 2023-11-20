package com.greenblat.cloudfilestorage.dto;

public record FolderCreateRequest(String folderName,
                                  String parentPath) {
}

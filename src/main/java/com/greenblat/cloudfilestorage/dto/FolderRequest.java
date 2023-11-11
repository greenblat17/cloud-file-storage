package com.greenblat.cloudfilestorage.dto;

public record FolderRequest(
        String parentFolderPath,
        String newFolder
) {
}

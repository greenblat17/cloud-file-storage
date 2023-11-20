package com.greenblat.cloudfilestorage.dto;

public record FileRenameRequest(String path,
                                String currentName,
                                String updatedName) {
}

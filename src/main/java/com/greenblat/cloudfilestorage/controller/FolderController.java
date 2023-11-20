package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FolderCreateRequest;
import com.greenblat.cloudfilestorage.dto.FolderUploadRequest;
import com.greenblat.cloudfilestorage.service.FileService;
import com.greenblat.cloudfilestorage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final FileService fileService;

    @PostMapping("/create")
    public String createFolder(FolderCreateRequest folderCreateRequest) {
        folderService.createFolder(folderCreateRequest);
        return "redirect:/login";
    }

    @PostMapping("/upload")
    public String uploadFolder(FolderUploadRequest folderUploadRequest) {
        fileService.uploadFiles(folderUploadRequest);
        return "redirect:/main";
    }

}

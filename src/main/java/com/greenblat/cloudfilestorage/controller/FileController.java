package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.service.FileService;
import com.greenblat.cloudfilestorage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute FileRequest fileRequest) {
        fileService.upload(fileRequest);
        return "redirect:/login";
    }

}
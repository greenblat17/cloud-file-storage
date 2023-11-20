package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FileRenameRequest;
import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute FileRequest fileRequest,
                             @RequestParam(defaultValue = "") String path) {
        fileService.upload(fileRequest, path);
        return "redirect:/main";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam String path,
                             @RequestParam String filename) {
        fileService.deleteFile(filename, path);
        return "redirect:/main";
    }

    @PostMapping("/rename")
    public String renameFile(FileRenameRequest fileRenameRequest) {
        fileService.renameFile(fileRenameRequest);
        return "redirect:/main";
    }

}
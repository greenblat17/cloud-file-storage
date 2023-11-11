package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FolderRequest;
import com.greenblat.cloudfilestorage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/create")
    public String createFolder(FolderRequest folderRequest) {
        folderService.createFolder(folderRequest);
        return "redirect:/login";
    }
}

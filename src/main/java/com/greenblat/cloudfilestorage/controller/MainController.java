package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.dto.FolderRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage(@ModelAttribute FolderRequest folderRequest,
                           @ModelAttribute FileRequest fileRequest) {
        return "main";
    }
}

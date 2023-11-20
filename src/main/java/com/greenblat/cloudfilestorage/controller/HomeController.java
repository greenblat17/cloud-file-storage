package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.FileRequest;
import com.greenblat.cloudfilestorage.dto.FolderRequest;
import com.greenblat.cloudfilestorage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FolderService folderService;

    @GetMapping("/main")
    public String mainPage(@ModelAttribute FileRequest fileRequest,
                           @ModelAttribute FolderRequest folderRequest,
                           @RequestParam(name = "path", defaultValue = "", required = false) String path,
                           Model model) {
        var pathResponses = folderService.getAllFoldersAndFiles(path);
        model.addAttribute("paths", pathResponses);
        model.addAttribute("links", folderService.getUrlForPath(path));
        model.addAttribute("currentUrl", folderService.getCurrentUrl(path));
        model.addAttribute("path", path);
        model.addAttribute("encodePath", URLEncoder.encode(path + "/", StandardCharsets.UTF_8));

        return "main";
    }
}

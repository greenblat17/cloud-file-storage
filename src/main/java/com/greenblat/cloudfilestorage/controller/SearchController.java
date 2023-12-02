package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.PathResponse;
import com.greenblat.cloudfilestorage.service.FolderService;
import com.greenblat.cloudfilestorage.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;
    private final FolderService folderService;

    @GetMapping
    public String search(@RequestParam(value = "query", defaultValue = "") String searchQuery, Model model) {
        Map<PathResponse, String> foundObjects;
        if (searchQuery.isEmpty()) {
            foundObjects = folderService.getAllFoldersAndFiles(searchQuery);
        } else {
            foundObjects = searchService.search(searchQuery);
        }
        model.addAttribute("foundObjects", foundObjects);
        return "search";
    }
}

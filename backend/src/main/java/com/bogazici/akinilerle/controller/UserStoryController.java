package com.bogazici.akinilerle.controller;

import com.bogazici.akinilerle.model.request.MultipleUserStory;
import com.bogazici.akinilerle.model.request.SingleUserStory;
import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class UserStoryController {

    private UserStoryService service;
    private static final String uploadingDir = System.getProperty("user.dir") + "/uploadingDir/";

    @Autowired
    public UserStoryController(UserStoryService service) {
        this.service = service;
    }

    @PostMapping("/analyse")
    public Report analyseSingleUserStory(@RequestBody SingleUserStory userStory){
        return service.analyseSingleUserStory(userStory.getUserStory());
    }

    @PostMapping("/analyse/txt")
    public List<Report> analyseMultipleUserStoryFile(@RequestParam("uploadingFiles") MultipartFile uploadingFile) throws IOException {
        return service.analyseMultipleUserStoryFile(uploadingFile);
    }

    @PostMapping("/analyse")
    public List<Report> analyseMultipleUserStory(@RequestBody MultipleUserStory userStories) {
        return service.analyseMultipleUserStory(userStories.getUserStoryList());
    }

}

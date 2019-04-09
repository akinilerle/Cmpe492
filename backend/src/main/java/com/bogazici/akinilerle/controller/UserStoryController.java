package com.bogazici.akinilerle.controller;

import com.bogazici.akinilerle.model.Report;
import com.bogazici.akinilerle.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserStoryController {

    private UserStoryService service;

    @Autowired
    public UserStoryController(UserStoryService service) {
        this.service = service;
    }

    @PostMapping("/analyse")
    public Report analyseSingleUserStory(@RequestBody String userStory){
        return service.analyseSingleUserStory(userStory);
    }

}

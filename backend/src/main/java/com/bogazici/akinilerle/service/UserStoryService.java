package com.bogazici.akinilerle.service;

import com.bogazici.akinilerle.analyser.UserStoryAnalyser;
import com.bogazici.akinilerle.model.Report;
import com.bogazici.akinilerle.model.UserStory;
import com.bogazici.akinilerle.parser.UserStoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserStoryService {

    private UserStoryParser userStoryParser;
    private UserStoryAnalyser userStoryAnalyser;

    @Autowired
    public UserStoryService(UserStoryParser userStoryParser, UserStoryAnalyser userStoryAnalyser) {
        this.userStoryParser = userStoryParser;
        this.userStoryAnalyser = userStoryAnalyser;
    }

    public Report analyseSingleUserStory(String userStoryString) {
        UserStory userStory = userStoryParser.parseSingle(userStoryString);
        if(Objects.isNull(userStory)){
            return Report.builder()
                    .type(Report.Type.ERROR)
                    .message("Format Error Mesajı")
                    .build();
        }

        Report sentenceReport = userStoryAnalyser.analyseSentence(userStory);
        if(sentenceReport.getType() == Report.Type.ERROR || sentenceReport.getType() == Report.Type.WARNING){
            return sentenceReport;
        }

        return null;
    }
}
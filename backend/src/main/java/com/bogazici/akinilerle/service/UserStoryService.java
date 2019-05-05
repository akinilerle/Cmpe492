package com.bogazici.akinilerle.service;

import com.bogazici.akinilerle.analyser.UserStoryAnalyser;
import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.model.UserStory;
import com.bogazici.akinilerle.parser.UserStoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if(Objects.isNull(userStory)){ //if user story is null, input string does not conform to allowed formats
            return Report.builder()
                    .type(Report.Type.ERROR)
                    .message("Format Error Mesajı") //TODO: mesajı düzelt
                    .story(userStoryString)
                    .build();
        }

        return userStoryAnalyser.analyseSentence(userStory);
    }

    public List<Report> analyseMultipleUserStoryFile(MultipartFile uploadingFile) throws IOException {
        if (!uploadingFile.isEmpty()) {
            //Construct the file in memory as list of strings
            byte[] bytes = uploadingFile.getBytes();
            String completeData = new String(bytes);
            String[] userStories = completeData.split("\n");
            List<String> userStoryList = Arrays.asList(userStories);

            return analyseMultipleUserStory(userStoryList);
        }
        else {
            throw new IllegalArgumentException("Boş Dosya");
        }
    }

    public List<Report> analyseMultipleUserStory(List<String> userStoryStringList) {
        List<Report> reportList = userStoryStringList.stream()
                .filter(s -> !s.equals(""))
                .map(this::analyseSingleUserStory) //analyse each one
                .collect(Collectors.toList());

        UserStory.Type mostUsedType = getMostUsedFormat(reportList);

        for(Report report: reportList){
            if(report.getUserStoryType() != mostUsedType){
                report.getMessages().add("Bu kullanıcı hikayesi diğer kullanıcı hikayelerinden farklı bir formattadır.");
                if(report.getType() == Report.Type.OK){
                    report.setType(Report.Type.WARNING);
                }
            }
        }
    }

    public UserStory.Type getMostUsedFormat(List<Report> reports){
        Map<UserStory.Type, Long> noOfUsages =
                reports.stream().collect(
                        Collectors.groupingBy(
                                Report::getUserStoryType, Collectors.counting()
                        )
                );

        return Collections.max(noOfUsages.entrySet(), Comparator.comparing(Map.Entry::getValue))
                .getKey();
    }


}
package com.bogazici.akinilerle.service;

import com.bogazici.akinilerle.analyser.UserStoryAnalyser;
import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.model.UserStory;
import com.bogazici.akinilerle.parser.UserStoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
                    .message("Kullanıcı hikayesi verilen formatlardan birine uymalıdır: " +
                            "\"Bir <ROL> olarak, <ISTEK> istiyorum. [Böylece, ]\", " +
                            "\"Bir <ROL> olarak,[ <SEBEP> için,] <ISTEK> istiyorum.\"")
                    .story(userStoryString)
                    .build();
        }
        return userStoryAnalyser.analyseSentence(userStory);
    }

    public List<Report> analyseMultipleUserStoryTxtFile(MultipartFile uploadingFile) throws IOException {
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


    public List<Report> analyseMultipleUserStoryCsvFile(MultipartFile uploadingFile) throws IOException {
        if (!uploadingFile.isEmpty()) {
            //Construct the file in memory as list of strings
            byte[] bytes = uploadingFile.getBytes();
            String completeData = new String(bytes);
            String[] rows = completeData.split("\n");
            if(rows.length==1){
                throw new IllegalArgumentException("Boş Dosya");
            }

            int indexOfUserStories = 0;

            //find the column that contains the user stories
            String firstRowParts[] = rows[0].split(";");
            for(int i=0;i<firstRowParts.length;i++){
                String lowerCasePart = firstRowParts[i].toLowerCase();
                if(lowerCasePart.equals("kullanıcı hikayesi") || lowerCasePart.equals("user story") ||
                        lowerCasePart.equals("kullanıcı hikayeleri") || lowerCasePart.equals("user stories")){
                    indexOfUserStories=i;
                    break;
                }
            }

            int finalIndexOfUserStories = indexOfUserStories;
            List<String> userStoryList = Arrays.asList(rows)
                                                .subList(1,rows.length)
                                                .stream()
                                                .filter(s -> !s.isEmpty())
                                                .map(s -> s.split(";")[finalIndexOfUserStories])
                                                .collect(Collectors.toList());

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
            if(report != null && report.getUserStoryType() != mostUsedType){

                ArrayList<String> newMessages = new ArrayList<>(report.getMessages());
                newMessages.add("Bu kullanıcı hikayesi diğer kullanıcı hikayelerinden farklı bir formattadır.");
                report.setMessages(newMessages);

                if(report.getType() == Report.Type.OK){
                    report.setType(Report.Type.WARNING);
                }
            }
        }
        return reportList;
    }

    public UserStory.Type getMostUsedFormat(List<Report> reports){
        Map<UserStory.Type, Long> noOfUsages =
                reports.stream()
                        .filter(Objects::nonNull)
                        .filter(r -> r.getUserStoryType() != null)
                        .collect(
                        Collectors.groupingBy(
                                Report::getUserStoryType, Collectors.counting()
                        )
                );

        if(noOfUsages.entrySet().size() == 0){
            return null;
        }
        else{
            return Collections.max(noOfUsages.entrySet(), Comparator.comparing(Map.Entry::getValue))
                    .getKey();
        }
    }


}
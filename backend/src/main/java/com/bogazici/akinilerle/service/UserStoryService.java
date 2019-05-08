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


    public String analyseMultipleUserStoryCsvFile(MultipartFile uploadingFile) throws IOException {
        if (!uploadingFile.isEmpty()) {
            //Construct the file in memory as list of strings
            byte[] bytes = uploadingFile.getBytes();
            String completeData = new String(bytes);
            String[] rows = completeData.split("\n");
            if(rows.length==1){
                throw new IllegalArgumentException("Boş Dosya");
            }

            //find the column that contains the user stories
            int indexOfUserStories = 0;
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

            List<String> rowList = Arrays.asList(rows)
                    .stream()
                    .filter(s -> !s.isEmpty())//filter empty rows
                    .filter(s -> !s.split(";")[finalIndexOfUserStories].equals("")) //get the user story columns
                    .collect(Collectors.toList());

            List<String> userStoryList = rowList
                    .subList(1,rowList.size())
                    .stream()
                    .map(s -> s.split(";")[finalIndexOfUserStories]) //get the user story columns
                    .collect(Collectors.toList());

            List<Report> reportList = analyseMultipleUserStory(userStoryList);

            StringBuilder sb = new StringBuilder();
            sb.append(rowList.get(0));
            sb.append(";Hatalar/Uyarılar");
            for(int i=0; i<reportList.size(); i++){
                sb.append("\n");
                sb.append(rowList.get(i+1));
                sb.append(";");
                int j = 1;
                for(String message: reportList.get(i).getMessages()){
                    sb.append(j);
                    sb.append(") ");
                    sb.append(message);
                    sb.append(" ");
                    j++;
                }
            }

            return sb.toString();
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


        //Check if the list of UserStories use the same format. If not, mark the least used formats with Warnings.
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

    /**
     * Checks the formats of the list of reports of user stories and returns the most used one
     * @param reports
     * @return most used user story type in the list
     */
    public UserStory.Type getMostUsedFormat(List<Report> reports){
        Map<UserStory.Type, Long> noOfUsages = //calculate the number of usages
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
        else{ //get and return the max
            return Collections.max(noOfUsages.entrySet(), Comparator.comparing(Map.Entry::getValue))
                    .getKey();
        }
    }


}
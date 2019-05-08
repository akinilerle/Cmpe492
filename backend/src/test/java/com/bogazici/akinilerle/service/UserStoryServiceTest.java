package com.bogazici.akinilerle.service;

import com.bogazici.akinilerle.analyser.UserStoryAnalyser;
import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.parser.UserStoryParser;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserStoryServiceTest {

    UserStoryService userStoryService;
    UserStoryAnalyser userStoryAnalyser;
    UserStoryParser userStoryParser;

    public UserStoryServiceTest() {
        userStoryAnalyser = new UserStoryAnalyser();
        userStoryParser = new UserStoryParser();
        this.userStoryService = new UserStoryService(userStoryParser,userStoryAnalyser);
    }

    @Test
    public void it_should_call_analyze_single_for_every_user_story_in_txt() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name.txt", ("Bir product manager olarak, kullanıcı hikayelerimin formata uygun olup olmadıklarını kontrol etmek istiyorum.\n" +
                "\n" +
                "Bir product manager olarak, kullanıcı hikayelerimin formatındaki hatalarını görebilmek istiyorum istiyorum.\n" +
                "\n" +
                "Bir product manager olarak, kullanıcı hikayelerimi excel dosyasından topluca kontrol edebilmek istiyorum.\n").getBytes());

        List<Report> reports = userStoryService.analyseMultipleUserStoryTxtFile(mockMultipartFile);

        assertEquals(3, reports.size());
    }

    @Test
    public void it_should_call_analyze_single_for_every_user_story_in_csv_excluding_empty_lines() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name.csv", ("asd;asd;asd;asd\n" +
                "Bir product manager olarak, kullanıcı hikayelerimin formata uygun olup olmadıklarını " +
                "kontrol etmek istiyorum;a ds d;lmlsdm; jasndla\n" +
                "\n" +
                "Bir product manager olarak, kullanıcı hikayelerimin formatındaki hatalarını " +
                "görebilmek istiyorum istiyorum.;asd ; asdsd; asd\n" +
                "\n" +
                "Bir product manager olarak, kullanıcı hikayelerimi excel dosyasından topluca kontrol " +
                "edebilmek istiyorum; asd ; asd; asd\n").getBytes());

        String response = userStoryService.analyseMultipleUserStoryCsvFile(mockMultipartFile);
        assertEquals(4, response.split("\n").length);
    }

    @Test
    public void it_should_call_analyze_single_for_every_user_story_that_are_not_in_the_first_row_in_csv() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name.csv",
                ("123;kullanıcı hikayeleri;asd;asd;asd\n" +
                "asd;Bir product manager olarak, kullanıcı hikayelerimin formata uygun " +
                "olup olmadıklarını kontrol etmek istiyorum;a ds d;lmlsdm; jasndla\n" +
                "vdaf;Bir product manager olarak, kullanıcı hikayelerimin formatındaki " +
                "hatalarını görebilmek istiyorum istiyorum.;asd ; asdsd; asd\n" +
                "asdafsa;Bir product manager olarak, kullanıcı hikayelerimi excel dosyasından " +
                "topluca kontrol edebilmek istiyorum; asd ; asd; asd\n").getBytes());

        String response = userStoryService.analyseMultipleUserStoryCsvFile(mockMultipartFile);
        assertEquals(4, response.split("\n").length);
    }

    @Test
    public void it_should_call_analyze_single_for_every_user_story_that_are_not_in_the_first_row_in_list_input() throws Exception {
        List<String> userStoryList = Arrays.asList("Bir product manager olarak, kullanıcı hikayelerimin formata uygun olup olmadıklarını kontrol etmek istiyorum.",
                "Bir product manager olarak, kullanıcı hikayelerimin formatındaki hatalarını görebilmek istiyorum istiyorum.",
                "Bir product manager olarak, kullanıcı hikayelerimi excel dosyasından topluca kontrol edebilmek istiyorum.");


        List<Report> reports = userStoryService.analyseMultipleUserStory(userStoryList);
        assertEquals(3, reports.size());
        for(Report report: reports){
            assertEquals(Report.Type.WARNING, report.getType());
        }
    }
}
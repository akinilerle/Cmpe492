package com.bogazici.akinilerle.controller;

import com.bogazici.akinilerle.model.request.MultipleUserStory;
import com.bogazici.akinilerle.model.request.SingleUserStory;
import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.service.UserStoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@Slf4j
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

    @PostMapping("/analyse/file")
    public ResponseEntity<InputStreamResource> analyseMultipleUserStoryFile(@RequestParam("file") MultipartFile uploadingFile) throws IOException {
        String fileName = uploadingFile.getOriginalFilename();
        if(Pattern.matches(".*\\.txt",fileName)){
            return analyseMultipleUserStoryTxtFile(uploadingFile);
        }
        else if(Pattern.matches(".*\\.csv",fileName)) {
            return analyseMultipleUserStoryCsvFile(uploadingFile);
        }
        else{
            throw new IllegalArgumentException("Yalnızca Txt ve Csv dosyaları desteklenmektedir.");
        }
    }


    @PostMapping("/analyse/txt")
    public ResponseEntity<InputStreamResource> analyseMultipleUserStoryTxtFile(@RequestParam("file") MultipartFile uploadingFile) throws IOException {
        List<Report> reports = service.analyseMultipleUserStoryTxtFile(uploadingFile);
        String responseString = createResponseStringTxt(reports);
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        InputStreamResource responseFileStream = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("asdasd", "kullaniciHikayeleri.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(responseString.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(responseFileStream);
    }

    @PostMapping("/analyse/csv")
    public ResponseEntity<InputStreamResource> analyseMultipleUserStoryCsvFile(@RequestParam("file") MultipartFile uploadingFile) throws IOException {
        String responseString = service.analyseMultipleUserStoryCsvFile(uploadingFile);
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        InputStreamResource responseFileStream = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("asdasd", "kullaniciHikayeleri.csv");

        return ResponseEntity.ok()
                .contentLength(responseString.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .headers(headers)
                .body(responseFileStream);
    }

    @PostMapping("/analyse/list")
    public List<Report> analyseMultipleUserStory(@RequestBody MultipleUserStory userStories) {
        return service.analyseMultipleUserStory(userStories.getUserStoryList());
    }

    private String createResponseStringTxt(List<Report> reports){
        StringBuilder sb = new StringBuilder();
        for(Report report: reports){
            sb.append(report.getStory());
            sb.append("\n");
            if(report.getMessages().isEmpty()){
                continue;
            }
            int i = 1;
            sb.append("\t\tHatalar/Uyarılar:\n");

            for(String message: report.getMessages()){
                sb.append("\t\t");
                sb.append(i);
                sb.append(") ");
                sb.append(message);
                sb.append("\n");
                i++;
            }
        }
        return sb.toString();
    }




}

package com.bogazici.akinilerle.analyser;

import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.model.UserStory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zemberek.core.turkish.PrimaryPos;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.lexicon.RootLexicon;
import zemberek.normalization.TurkishSpellChecker;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserStoryAnalyser {

    private TurkishMorphology turkishMorphology;
    private TurkishSpellChecker spellChecker;

    public UserStoryAnalyser() {
        turkishMorphology = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .build();
        try {
            spellChecker = new TurkishSpellChecker(turkishMorphology);
        } catch (IOException e) {
            System.out.println("Could not initialize turkish spell checker");
            e.printStackTrace();
        }
    }

    public Report analyseSentence(UserStory userStory) {
        Report spellCheckReport = spellCheck(userStory);
        Report sentenceReport = checkSentence(userStory);

        if(spellCheckReport == null && sentenceReport == null){ //both checks have no warning or errors
            return Report.builder()
                    .type(Report.Type.OK)
                    .story(userStory.toString())
                    .userStoryType(userStory.getType())
                    .build();
        }
        else if (sentenceReport == null){ //spell check found a problem, sentence check did not
            spellCheckReport.setUserStoryType(userStory.getType());
            return spellCheckReport;
        }
        else if (spellCheckReport == null){ //sentence check found a problem, spell check did not
            return sentenceReport;
        }
        else{ //both checks found problems, reports need to be merged
            Report.ReportBuilder reportBuilder = Report.builder();
            reportBuilder.story(sentenceReport.getStory());
            reportBuilder.userStoryType(sentenceReport.getUserStoryType());
            reportBuilder.type(sentenceReport.getType());
            for(String message: spellCheckReport.getMessages()){
                reportBuilder.message(message);
            }
            for(String message: sentenceReport.getMessages()){
                reportBuilder.message(message);
            }
            return reportBuilder.build();
        }
    }

    /**
     * Spell checks words in the user story.
     * @param userStory
     * @return Report object containgin the suggestion message, or null if there are no misspellings
     */
    private Report spellCheck(UserStory userStory){
        if(spellChecker == null){
            return null;
        }
        Report.ReportBuilder builder = Report.builder().type(Report.Type.WARNING);

        String fullStory =  userStory.getRole() + " " + userStory.getRequest()
                + (StringUtils.isEmpty(userStory.getBenefit()) ? "" : " " + userStory.getBenefit());

        for(String word: fullStory.split(" ")){ //check spelling of every word
            if (!spellChecker.check(word)){ //if the word is misspelled
                List<String> suggestions = spellChecker.suggestForWord(word);
                String errorMessage = "Yazım hatası: \"" + word + "\". Bunu mu demek istediniz? : "
                        + (suggestions.size() <= 3 ? suggestions : suggestions.subList(0,3)); //add suggestions

                builder.message(errorMessage).story(userStory.toString());
            }
        }

        Report report = builder.build();

        if(report.getMessages().isEmpty()){//no misspellings found
            return null;
        }
        else {
            return report;
        }
    }

    /**
     * Checks the number of verbs in role,request and benefit parts of the user story.
     * @param userStory
     * @return Report containing the error message and the verblist. returns null if there is no error
     */
    private Report checkSentence(UserStory userStory){
        List<String> roleVerbs = getVerbs(userStory.getRole());
        Report.ReportBuilder builder = Report.builder()
                .type(Report.Type.ERROR);

        if(Pattern.matches(".*\\(.*\\).*",userStory.getOriginalString())){ //Marks additional notes inside parantheses that violates minimal quality
            builder.message("Kullanıcı hikayesi minimal olmalı, parantez içinde ekstra açıklama içermemelidir.");
        }

        if(roleVerbs.size() > 0){
            return builder
                    .message("Kullanıcı hikayesinin rol kısmı yüklem içermemelidir. Yüklemler: " + roleVerbs)
                    .build();
        }

        List<String> benefitVerbs = null;
        if(!userStory.getType().equals(UserStory.Type.TYPE_RR)){
            benefitVerbs = getVerbs(userStory.getBenefit());
        }
        if(userStory.getType().equals(UserStory.Type.TYPE_RBR) && benefitVerbs.size() > 0){
            return builder
                    .message("Kullanıcı hikayesinin fayda kısmı yüklem içermemelidir. Yüklemler: " + benefitVerbs)
                    .build();
        }
        else if(userStory.getType().equals(UserStory.Type.TYPE_RRB) && benefitVerbs.isEmpty()){
            return builder
                    .message("Kullanıcı hikayesinin fayda kısmı yüklemli bir cümle olmalıdır: " + userStory.getBenefit())
                    .build();
        }

        return null;
    }

    /**
     * get the verbs in a given sentence.
     * @param sentence
     * @return list of strings containing the verbs.
     */
    private List<String> getVerbs(String sentence){

        return turkishMorphology.analyzeAndDisambiguate(sentence)
                .bestAnalysis()
                .stream()
                .filter(sa -> sa.getPos().equals(PrimaryPos.Verb))
                .map(sa -> sa.getStem() + sa.getEnding())
                .collect(Collectors.toList());
    }
}

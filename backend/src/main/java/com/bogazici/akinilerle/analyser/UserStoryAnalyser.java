package com.bogazici.akinilerle.analyser;

import com.bogazici.akinilerle.model.Report;
import com.bogazici.akinilerle.model.UserStory;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zemberek.core.turkish.PrimaryPos;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.lexicon.RootLexicon;
import zemberek.normalization.TurkishSpellChecker;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserStoryAnalyser {
    public Report analyseSentence(UserStory userStory) {
        Report report = spellCheck(userStory);
        if(Objects.nonNull(report)){
            return report;
        }
        report = checkSentence(userStory);
        return null;
    }

    private Report spellCheck(UserStory userStory){
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        TurkishSpellChecker spellChecker;
        try {
            spellChecker = new TurkishSpellChecker(morphology);
        } catch (IOException e) {
            System.out.println("Could not initialize turkish spell checker");
            e.printStackTrace();
            return null;
        }
        Report.ReportBuilder builder = Report.builder().type(Report.Type.WARNING);
        String fullStory =  userStory.getRole() + " " + userStory.getRequest()
                + (StringUtils.isEmpty(userStory.getBenefit()) ? "" : " " + userStory.getBenefit());

        for(String word: fullStory.split(" ")){
            if (!spellChecker.check(word)){
                List<String> suggestions = spellChecker.suggestForWord(word);
                String errorMessage = "Yazım hatası: \"" + word + "\". Bunu mu demek istediniz? : "
                        + (suggestions.size() <= 3 ? suggestions : suggestions.subList(0,3));

                builder.message(errorMessage);
            }
        }
        Report report = builder.build();

        if(report.getMessages().isEmpty()){
            return null;
        }
        else {
            return report;
        }
    }

    private Report checkSentence(UserStory userStory){
        List<String> roleVerbs = getVerbs(userStory.getRole());
        if(roleVerbs.size() > 0){
            System.out.println("Kullanıcı hikayesinin rol kısmı yüklem içermemelidir: " + roleVerbs);
            return Report.builder()
                    .type(Report.Type.ERROR)
                    .message("Kullanıcı hikayesinin rol kısmı yüklem içermemelidir: " + roleVerbs)
                    .build();
        }

        List<String> benefitVerbs = null;
        if(!userStory.getType().equals(UserStory.Type.TYPE_RR)){
            benefitVerbs = getVerbs(userStory.getBenefit());
        }
        if(userStory.getType().equals(UserStory.Type.TYPE_RBR) && benefitVerbs.size() > 0){
            System.out.println("Kullanıcı hikayesinin fayda kısmı yüklem içermemelidir: " + benefitVerbs);
            return Report.builder()
                    .type(Report.Type.ERROR)
                    .message("Kullanıcı hikayesinin fayda kısmı yüklem içermemelidir: " + benefitVerbs)
                    .build();
        }
        else if(userStory.getType().equals(UserStory.Type.TYPE_RRB) && benefitVerbs.isEmpty()){
            System.out.println("Kullanıcı hikayesinin fayda kısmı yüklemli bir cümle olmalıdır: " + userStory.getBenefit());
            return Report.builder()
                    .type(Report.Type.ERROR)
                    .message("Kullanıcı hikayesinin fayda kısmı yüklemli bir cümle olmalıdır: " + userStory.getBenefit())
                    .build();
        }

        return null;
    }

    private List<String> getVerbs(String sentence){
        TurkishMorphology turkishMorphology = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .build();

        return turkishMorphology.analyzeAndDisambiguate(sentence)
                .bestAnalysis()
                .stream()
                .filter(sa -> sa.getPos().equals(PrimaryPos.Verb))
                .map(sa -> sa.getStem() + sa.getEnding())
                .collect(Collectors.toList());
    }
}

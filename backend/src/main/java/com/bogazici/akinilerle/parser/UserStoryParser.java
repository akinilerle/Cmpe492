package com.bogazici.akinilerle.parser;

import com.bogazici.akinilerle.model.UserStory;
import com.bogazici.akinilerle.model.response.Report;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserStoryParser {

    private static final String FORMAT_1_REGEX = ".*olarak.*(istiyor(um|uz)|ihtiyacım(ız)? var).*böylece.*";
    private static final String FORMAT_2_REGEX = ".*olarak.* için .*(istiyor(um|uz)|ihtiyacım(ız)? var)";
    private static final String FORMAT_3_REGEX = ".*olarak.*(istiyor(um|uz)|ihtiyacım(ız)? var)";

    public UserStory parseSingle(String originalUserStory){
        String normalizedUserStory = normalize(originalUserStory);
        String[] words = normalizedUserStory.split(" ");

        if(Pattern.matches(FORMAT_1_REGEX,normalizedUserStory)){
            return parseFormat1(words,originalUserStory);
        }
        else if(Pattern.matches(FORMAT_2_REGEX,normalizedUserStory)){
            return parseFormat2(words,originalUserStory);
        }
        else if(Pattern.matches(FORMAT_3_REGEX,normalizedUserStory)){
            return parseFormat3(words,originalUserStory);
        }
        else{
            return null;
        }
    }

    private UserStory parseFormat1(String[] words, String originalStory) {
        Map.Entry<String, Integer> roleVals = parseRole(words, 0);
        String rolePart = roleVals.getKey();
        int i = roleVals.getValue();

        Map.Entry<String, Integer> requestVals = parseRequest(words, i);
        String requestPart = requestVals.getKey();
        i = requestVals.getValue();

        List benefitWordList = Arrays.asList(words).subList(i,words.length);
        String benefitPart = String.join(" ", benefitWordList);

        return new UserStory(rolePart,requestPart,benefitPart,originalStory,UserStory.Type.TYPE_RRB);
    }

    private UserStory parseFormat2(String[] words, String originalStory) {
        Map.Entry<String, Integer> roleVals = parseRole(words, 0);
        String rolePart = roleVals.getKey();
        int i = roleVals.getValue();

        Map.Entry<String, Integer> benefitVals = parseBenefit(words, i);
        String benefitPart = benefitVals.getKey();
        i = benefitVals.getValue();

        List requestWordList = Arrays.asList(words).subList(i,words.length);
        String requestPart = String.join(" ", requestWordList);

        return new UserStory(rolePart,requestPart,benefitPart,originalStory,UserStory.Type.TYPE_RBR);
    }

    private UserStory parseFormat3(String[] words, String originalStory) {
        Map.Entry<String, Integer> roleVals = parseRole(words, 0);
        String rolePart = roleVals.getKey();
        int i = roleVals.getValue();

        List requestWordList = Arrays.asList(words).subList(i,words.length);
        String requestPart = String.join(" ", requestWordList);

        return new UserStory(rolePart,requestPart,null,originalStory,UserStory.Type.TYPE_RR);
    }

    /** Extracts role part of the user story
     *
     * @param words
     * @param startIndex
     * @return Role part as key, and next index to parse as value
     */
    private Map.Entry<String,Integer> parseRole(String[] words, int startIndex) {
        int i = startIndex;
        ArrayList<String> roleWords = new ArrayList<>();

        while(!words[i].equals("olarak")){
            roleWords.add(words[i]);
            i++;
        }
        roleWords.add(words[i]);
        return new HashMap.SimpleEntry<>(String.join(" ",roleWords), ++i);
    }

    /** Extracts request part of the user story
     *
     * @param words
     * @param startIndex
     * @return Request part as key, and next index to parse as value
     */
    private Map.Entry<String,Integer> parseRequest(String[] words, int startIndex) {
        int i = startIndex;
        HashSet<String> requestKeyWords1 = Sets.newHashSet("istiyorum", "istiyoruz");
        HashSet<String> requestKeyWords2 = Sets.newHashSet("ihtiyacım", "ihtiyacımız");

        ArrayList<String> requestWords = new ArrayList<>();

        while(true){
            requestWords.add(words[i]);
            if(requestKeyWords1.contains(words[i])){
                break;
            }
            else if(requestKeyWords2.contains(words[i]) && words[i+1].equals("var")){
                requestWords.add(words[++i]);
                break;
            }
            i++;
        }

        return new HashMap.SimpleEntry<>(String.join(" ",requestWords), ++i);
    }

    /** Extracts benefit part of the user story
     *
     * @param words
     * @param startIndex
     * @return Benefit part as key, and next index to parse as value
     */
    private Map.Entry<String,Integer> parseBenefit(String[] words, int startIndex) {
        int i = startIndex;
        ArrayList<String> benefitWords = new ArrayList<>();

        while(!words[i].equals("için")){
            benefitWords.add(words[i]);
            i++;
        }

        benefitWords.add(words[i]);

        return new HashMap.SimpleEntry<>(String.join(" ",benefitWords), ++i);
    }

    /**
     * Removes every non alpha numeric character
     * @param input
     * @return normalized string
     */
    private String normalize(String input) {
        return input.replaceAll("[^a-zA-Z0-9 ÇçĞğİıÖöŞşÜü]", "").toLowerCase();
    }

}
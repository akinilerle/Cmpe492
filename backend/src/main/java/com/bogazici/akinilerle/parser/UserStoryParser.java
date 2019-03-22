package com.bogazici.akinilerle.parser;

import com.bogazici.akinilerle.model.UserStory;

public class UserStoryParser {

    private static final String FORMAT_1_REGEX = ".*olarak.*(istiyor(um|uz)|ihtiyacım(ız)? var).*böylece.*";
    private static final String FORMAT_2_REGEX = ".*olarak.*için.*(istiyor(um|uz)|ihtiyacım(ız)? var)";
    private static final String FORMAT_3_REGEX = ".*olarak.*(istiyor(um|uz)|ihtiyacım(ız)? var)";

    public UserStory parseSingle(String userStory){
        return null;
    }

}

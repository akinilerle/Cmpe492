package com.bogazici.akinilerle.model.response;

import com.bogazici.akinilerle.model.UserStory;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Report {

    public enum Type{
        WARNING,
        ERROR,
        OK
    }

    @Singular
    List<String> messages = new ArrayList<>();
    UserStory.Type userStoryType;
    String story;
    Type type;

}
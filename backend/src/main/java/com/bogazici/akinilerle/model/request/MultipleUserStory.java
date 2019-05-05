package com.bogazici.akinilerle.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class MultipleUserStory {

    @NotEmpty
    List<String> userStoryList;
}

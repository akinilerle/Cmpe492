package com.bogazici.akinilerle.model.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class SingleUserStory {

    @NonNull
    String userStory;
}

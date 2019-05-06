package com.bogazici.akinilerle.model.request;

import lombok.NonNull;

public class SingleUserStory {

    @NonNull
    private String userStory;

    public SingleUserStory() {
    }

    public SingleUserStory(@NonNull String userStory) {
        this.userStory = userStory;
    }

    public String getUserStory() {
        return userStory;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
    }
}

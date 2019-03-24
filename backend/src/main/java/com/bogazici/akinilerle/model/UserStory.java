package com.bogazici.akinilerle.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserStory {

    public enum Type{
        TYPE_1,
        TYPE_2,
        TYPE_3
    }

    private String role;
    private String request;
    private String benefit;
    private Type type;
}

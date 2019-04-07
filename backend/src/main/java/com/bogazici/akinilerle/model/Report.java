package com.bogazici.akinilerle.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

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
    List<String> messages;

    Type type;

}

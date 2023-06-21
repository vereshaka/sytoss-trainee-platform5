package com.sytoss.checktask.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CheckTaskParameters {

    private String answer;

    private String etalon;

    private String script;

    private List<String> conditions = new ArrayList<>();
}

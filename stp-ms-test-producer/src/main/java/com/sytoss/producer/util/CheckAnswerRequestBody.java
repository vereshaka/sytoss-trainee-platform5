package com.sytoss.producer.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckAnswerRequestBody {

    private String answer;

    private String etalon;

    private String script;
}

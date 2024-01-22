package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckRequestParameters {

    private String request;

    private String script;

    private String checkAnswer;

    private boolean isSortingRelevant;

    private String mode;

    public boolean isQueryForUpdate(){
        if(request != null){
            return !request.toUpperCase().startsWith("SELECT");
        }else{
            return false;
        }
    }
}

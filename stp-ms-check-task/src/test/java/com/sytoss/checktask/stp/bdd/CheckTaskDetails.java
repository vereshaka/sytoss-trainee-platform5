package com.sytoss.checktask.stp.bdd;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTaskDetails {

    private CheckTaskParameters checkTaskParameters = new CheckTaskParameters();

    private Score grade;
}

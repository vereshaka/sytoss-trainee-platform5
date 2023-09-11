package com.sytoss.checktask.stp.bdd.common;

import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTaskDetails {

    private CheckTaskParameters checkTaskParameters = new CheckTaskParameters();

    private Score score;
}

package com.sytoss.checktask.stp.controller;

import bom.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/task/check")
public class CheckTaskController {

    private final GradeService gradeService;

    @RequestMapping(method = RequestMethod.POST)
    public void check(
            @RequestBody CheckAnswerRequestBody body) throws Exception {
        gradeService.checkAndGrade(body);
    }

  /*  private bom.CheckTaskParameters convertJSONtoCheckAnswerRequestBody(String body) throws org.json.simple.parser.ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer((String) jsonObject.get("answer"));
        checkAnswerRequestBody.setEtalon((String) jsonObject.get("etalon"));
        JSONObject subJSONObject = (JSONObject) jsonObject.get("script");
        checkAnswerRequestBody.setScript(subJSONObject.toJSONString());
        return checkAnswerRequestBody;
    }*/
}

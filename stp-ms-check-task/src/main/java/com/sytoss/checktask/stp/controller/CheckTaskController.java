package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.stp.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/task/check")
public class CheckTaskController {

    private final GradeService gradeService;

    @RequestMapping(method = RequestMethod.POST)
    public void checkTask(
            @RequestBody String body) throws Exception {
        CheckAnswerRequestBody checkAnswerRequestBody = convertJSONtoCheckAnswerRequestBody(body);
        gradeService.checkAndGrade(checkAnswerRequestBody);
    }

    private CheckAnswerRequestBody convertJSONtoCheckAnswerRequestBody(String body) throws org.json.simple.parser.ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer((String) jsonObject.get("answer"));
        checkAnswerRequestBody.setEtalon((String) jsonObject.get("etalon"));
        JSONObject subJSONObject = (JSONObject) jsonObject.get("script");
        checkAnswerRequestBody.setScript(subJSONObject.toJSONString());
        return checkAnswerRequestBody;
    }
}

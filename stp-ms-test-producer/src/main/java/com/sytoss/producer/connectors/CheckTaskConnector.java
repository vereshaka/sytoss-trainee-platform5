package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.producer.util.CheckAnswerRequestBody;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Headers({"Content-type: application/json", "Accept: application/json"})
@FeignClient(name = "answerClient", url = "${check-task-url}")
public interface CheckTaskConnector {

    @PostMapping("${check-task-endpoint}")
    Grade checkAnswer(@RequestBody CheckAnswerRequestBody checkAnswerRequestBody);
}

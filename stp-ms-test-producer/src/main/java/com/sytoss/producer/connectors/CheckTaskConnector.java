package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Headers({"Content-type: application/json", "Accept: application/json"})
@FeignClient(name = "answerClient", url = "${check-task-url}")
public interface CheckTaskConnector {

    @PostMapping("/task/check")
    Score checkAnswer(@RequestBody CheckTaskParameters checkTaskParameters);


    @PostMapping("/task/check-request")
    QueryResult testAnswer(@RequestBody CheckRequestParameters body) ;
}

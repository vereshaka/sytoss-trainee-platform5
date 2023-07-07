package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Headers({"Content-type: application/json", "Accept: application/json"})
@FeignClient(name = "answerClient", url = "${check-task-url}")
public interface CheckTaskConnector {

    @PostMapping("/api/task/check")
    Score checkAnswer(@RequestBody CheckTaskParameters checkTaskParameters);
}

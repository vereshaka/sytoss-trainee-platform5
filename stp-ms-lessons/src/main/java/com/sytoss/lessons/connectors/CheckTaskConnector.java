package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9010/api", name = "checkTaskConnector")
public interface CheckTaskConnector {

    @GetMapping("/task/check-etalon")
    IsCheckEtalon checkEtalon(@RequestBody CheckRequestParameters body);

    @GetMapping("/task/check-request")
    QueryResult checkRequest(@RequestBody CheckRequestParameters body);
}

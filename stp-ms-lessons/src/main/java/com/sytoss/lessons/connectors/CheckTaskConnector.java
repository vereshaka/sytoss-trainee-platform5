package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.personalexam.CheckEtalonParametrs;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${check-task-url}", name = "checkTaskConnector")
public interface CheckTaskConnector {

    @GetMapping("task/check-etalon")
    IsCheckEtalon checkEtalon(@RequestBody CheckEtalonParametrs body);
}

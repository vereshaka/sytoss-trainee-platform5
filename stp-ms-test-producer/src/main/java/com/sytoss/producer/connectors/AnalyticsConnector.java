package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.personalexam.Score;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${lessons-url}", name = "analyticsConnector")
public interface AnalyticsConnector {

    @PostMapping("/analytics/update")
    Score checkAnswer(@RequestBody AnalyticsElement analyticsElement);
}

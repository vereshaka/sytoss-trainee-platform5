package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.domain.bom.personalexam.Score;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${lessons-url}", name = "ratingConnector")
public interface RatingConnector {

    @PostMapping("/rating/update")
    Score checkAnswer(@RequestBody Rating rating);
}

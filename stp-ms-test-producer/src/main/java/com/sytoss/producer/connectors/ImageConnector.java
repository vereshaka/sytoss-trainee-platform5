package com.sytoss.producer.connectors;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9104/api", name = "ImageConnector")
public interface ImageConnector {
    @PostMapping("/convert/image")
    Long convertImage(@RequestBody String question);
}

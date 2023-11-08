package com.sytoss.producer.connectors;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${image-provider.url}", name = "ImageConnector")
public interface ImageConnector {
    @PostMapping("/image/convert")
    Long convertImage(@RequestBody String question);
}

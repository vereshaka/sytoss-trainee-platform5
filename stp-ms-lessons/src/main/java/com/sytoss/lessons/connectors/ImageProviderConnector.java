package com.sytoss.lessons.connectors;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${image-provider-url}", name = "imageProviderConnector")
public interface ImageProviderConnector {

    @PostMapping("save-image")
    String saveImage(@RequestBody byte[] image);

    @PostMapping(value = "image/byte/{name}")
    void saveImageByteWithName(@PathVariable("name") String name, @RequestBody byte[] image);
}

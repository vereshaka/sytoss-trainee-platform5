package com.sytoss.users.connectors;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(url = "${image-provider-url}", name = "imageProviderConnector")
public interface ImageProviderConnector {

    @PostMapping("api/save-image")
    String saveImage(@RequestBody byte[] image);

    @PostMapping(value = "public/api/image/{name}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void saveImage(@PathVariable("name") String name, @RequestPart("image") MultipartFile image);
}

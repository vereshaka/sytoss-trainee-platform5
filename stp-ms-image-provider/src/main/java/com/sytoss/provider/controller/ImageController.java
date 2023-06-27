package com.sytoss.provider.controller;

import com.sytoss.provider.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/convert/image")
    public Long convertImage(@RequestBody String question){
        return imageService.generatePngFromQuestion(question);
    }
}

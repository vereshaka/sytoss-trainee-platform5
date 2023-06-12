package com.sytoss.users.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdatePhotoRequestParams {

    private String email;

    private MultipartFile photo;
}

package com.sytoss.producer.bom;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScreenshotModel {

    private String personalExamId;

    private String userUid;

    private Long userId;

    private String email;

    private String imageFileName;

    private Date date;
}

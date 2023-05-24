package com.sytoss.lessons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
        "com.sytoss.lessons"})
public class LessonsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LessonsApplication.class, args);
    }
}

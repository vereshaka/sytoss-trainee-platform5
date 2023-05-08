package com.sytoss.csm;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import liquibase.sdk.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class CSMApplication {
    public static void main(String[] args) {
        SpringApplication.run(CSMApplication.class, args);
    }

}

package com.sytoss.stp;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = CheckTaskApplication.class)
public class CucumberConfiguration {

}

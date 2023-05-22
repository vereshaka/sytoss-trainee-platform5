package com.sytoss.stp;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CheckTaskApplication.class)
@CucumberContextConfiguration
public class CucumberConfiguration {

}

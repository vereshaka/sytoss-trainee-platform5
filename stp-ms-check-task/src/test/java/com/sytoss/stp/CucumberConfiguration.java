package com.sytoss.stp;

import com.sytoss.stp.service.GradeService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = CheckTaskApplication.class)
public class CucumberConfiguration {

}

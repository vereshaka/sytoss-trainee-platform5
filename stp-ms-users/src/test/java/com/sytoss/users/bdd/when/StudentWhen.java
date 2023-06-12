package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.util.UpdatePhotoRequestParams;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class StudentWhen extends CucumberIntegrationTest {

    @When("^photo of student with email \"(.*)\" is updated$")
    public void updateStudentPhoto(String email) {
        String url = "/api/student/updatePhoto";
//        MultipartFile photo = Mockito.mock(MultipartFile.class);
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        MultipartFile photo = new MockMultipartFile("photo.jpg", photoBytes);
        UpdatePhotoRequestParams requestParams = new UpdatePhotoRequestParams();
        requestParams.setEmail(email);
        requestParams.setPhoto(photo);
        ResponseEntity<String> responseEntity = doPost(url, requestParams, new ParameterizedTypeReference<String>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}

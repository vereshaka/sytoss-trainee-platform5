package com.sytoss.producer;

import com.sytoss.producer.services.RequestContextThreadLocal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractSTPJunitTest {

    @BeforeEach
    public void setup() {
        RequestContextThreadLocal.getRequestContext().setUserName("testUser");
    }

    @AfterEach
    public void tearDown() {
        RequestContextThreadLocal.drop();
    }
}

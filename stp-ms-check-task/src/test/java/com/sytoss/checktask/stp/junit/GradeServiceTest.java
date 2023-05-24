package com.sytoss.checktask.stp.junit;

import com.sytoss.checktask.stp.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.service.GradeService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GradeServiceTest extends com.sytoss.stp.junit.DatabaseInitHelper {

    private final GradeService gradeService = mock(GradeService.class);

    @Test
    void checkAndGrade() throws Exception {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select * from etalon");
        checkAnswerRequestBody.setScript("[{\n" +
                "    \"databaseChangeLog\": [\n" +
                "      {\n" +
                "        \"changeSet\": {\n" +
                "          \"id\": \"create_answer\",\n" +
                "          \"author\": \"ivan-larin\",\n" +
                "          \"changes\": [\n" +
                "            {\n" +
                "              \"createTable\": {\n" +
                "                \"tableName\": \"Answer\",\n" +
                "                \"columns\": [\n" +
                "                  {\n" +
                "                    \"column\": {\n" +
                "                      \"name\": \"Answer\",\n" +
                "                      \"type\": \"varchar\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"createTable\": {\n" +
                "                \"tableName\": \"Etalon\",\n" +
                "                \"columns\": [\n" +
                "                  {\n" +
                "                    \"column\": {\n" +
                "                      \"name\": \"Etalon\",\n" +
                "                      \"type\": \"varchar\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"changeSet\": {\n" +
                "          \"id\": \"insert-answer\",\n" +
                "          \"changes\": [\n" +
                "            {\n" +
                "              \"insert\": {\n" +
                "                \"columns\": [\n" +
                "                  {\n" +
                "                    \"column\": {\n" +
                "                      \"name\": \"Answer\",\n" +
                "                      \"value\": \"it_is_answer\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"tableName\": \"Answer\"\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"insert\": {\n" +
                "                \"columns\": [\n" +
                "                  {\n" +
                "                    \"column\": {\n" +
                "                      \"name\": \"Etalon\",\n" +
                "                      \"value\": \"it_is_etalon\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"tableName\": \"Etalon\"\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]}]");
        gradeService.checkAndGrade(checkAnswerRequestBody);
        verify(gradeService).checkAndGrade(checkAnswerRequestBody);
    }
}
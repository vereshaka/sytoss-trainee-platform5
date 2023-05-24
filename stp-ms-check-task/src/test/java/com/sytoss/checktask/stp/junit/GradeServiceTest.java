package com.sytoss.checktask.stp.junit;

import bom.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.service.GradeService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class GradeServiceTest extends DatabaseInitHelper{

    private final GradeService gradeService = mock(GradeService.class);

    @Test
    void checkAndGrade() throws Exception {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select * from etalon");
        checkAnswerRequestBody.setScript("""
                [{
                    "databaseChangeLog": [
                      {
                        "changeSet": {
                          "id": "create_answer",
                          "author": "ivan-larin",
                          "changes": [
                            {
                              "createTable": {
                                "tableName": "Answer",
                                "columns": [
                                  {
                                    "column": {
                                      "name": "Answer",
                                      "type": "varchar"
                                    }
                                  }
                                ]
                              }
                            },
                            {
                              "createTable": {
                                "tableName": "Etalon",
                                "columns": [
                                  {
                                    "column": {
                                      "name": "Etalon",
                                      "type": "varchar"
                                    }
                                  }
                                ]
                              }
                            }
                          ]
                        }
                      },
                      {
                        "changeSet": {
                          "id": "insert-answer",
                          "changes": [
                            {
                              "insert": {
                                "columns": [
                                  {
                                    "column": {
                                      "name": "Answer",
                                      "value": "it_is_answer"
                                    }
                                  }
                                ],
                                "tableName": "Answer"
                              }
                            },
                            {
                              "insert": {
                                "columns": [
                                  {
                                    "column": {
                                      "name": "Etalon",
                                      "value": "it_is_etalon"
                                    }
                                  }
                                ],
                                "tableName": "Etalon"
                              }
                            }
                          ]
                        }
                      }
                    ]}]""");
        gradeService.checkAndGrade(checkAnswerRequestBody);
        verify(gradeService).checkAndGrade(checkAnswerRequestBody);
    }
}
Feature: check answer

  Scenario: Check student's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from etalon"
    And check SQL is "select * from answer"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 10
    And Grade message is "ok"
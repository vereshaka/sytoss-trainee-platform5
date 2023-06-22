Feature: check answer

  Scenario: Check correct student's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Authors"
    And check SQL is "select * from Authors"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is "ok"

  Scenario: Check wrong student's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Books"
    And check SQL is "select * from Authors"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0
    And Grade message is "not ok"
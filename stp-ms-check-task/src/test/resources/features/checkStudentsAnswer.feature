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

  Scenario: Check student's answer with condition
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Authors ORDER BY Name"
    And check SQL is "select * from Authors"
    And answer should contains "ORDER BY" condition with "CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0.7
    And Grade message is "ok"

  Scenario: Check etalon's answer
    Given Request contains database script as in "script1.yml"
    And check SQL is "select * from Books"
    When request sent to check etalon answer
    Then request should be processed successfully
    And should return that etalon is valid

  Scenario: Check not valid etalon's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Pages"
    When request sent to check etalon answer
    Then request should be processed successfully
    And should return that etalon is not valid

  Scenario: Check current сorrect student's answer
    Given Request contains database script as in "script1.yml"
    And check SQL is "select * from Authors"
    When request sent to check "request"
    Then request should be processed successfully
    And query result should be
      | id | name        |
      | 1  | Ivan Ivanov |
      | 2  | Ivan Petrov |

  Scenario: Check current incorrect student's answer
    Given Request contains database script as in "script1.yml"
    And check SQL is "select Authors"
    When request sent to check "request"
    Then operation should be finished with "406" error
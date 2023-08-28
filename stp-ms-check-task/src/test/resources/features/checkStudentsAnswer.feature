Feature: check answer

  Scenario: Check correct student's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Discipline"
    And check SQL is "select * from Discipline"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is "ok"

  Scenario: Check wrong student's answer
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Discipline"
    And check SQL is "select * from Topic"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0
    And Grade message is "not ok"

  Scenario: Check student's answer with condition
    Given Request contains database script as in "script1.yml"
    And etalon SQL is "select * from Discipline ORDER BY id"
    And check SQL is "select * from Discipline"
    And answer should contains "ORDER BY" condition with "CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0.7
    And Grade message is "ok"

  Scenario: Check etalon's answer
    Given Request contains database script as in "script1.yml"
    And check SQL is "select * from Discipline"
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
    And check SQL is "select * from Discipline"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | id | name  |
      | 1  | SQL   |
      | 2  | Mongo |

  Scenario: Check current incorrect student's answer
    Given Request contains database script as in "script1.yml"
    And check SQL is "select * fr Discipline"
    When request sent to check incorrect script
    Then operation should be finished with "406" error

  Scenario: Check correct sequence of columns
    Given Request contains database script as in "script1.yml"
    And check SQL is "select name,id from Discipline"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | name  | id |
      | SQL   | 1  |
      | Mongo | 2  |
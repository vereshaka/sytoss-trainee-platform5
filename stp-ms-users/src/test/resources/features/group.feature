Feature: Group

  Background:
    Given student with "Ivan" firstName and "Ivanovich" lastName and "test@gmail.com" email exists

  Scenario: Assignee student to group
    Given  "AT-21-2" group exist
    When teacher assignee this student to "AT-21-2" group
    Then operation is successful
    And this student should have "AT-21-2" group

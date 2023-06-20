Feature: Group

  Scenario: Assignee student to group
    Given student with "Ivan" firstName and "Ivanovich" lastName and "test@gmail.com" email exists
    And "AT-21-2" group exist
    When teacher assignee this student to "AT-21-2" group
    Then operation is successful
    And this student should have "AT-21-2" group

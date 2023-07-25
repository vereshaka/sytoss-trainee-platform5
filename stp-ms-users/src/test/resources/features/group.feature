Feature: Group

  Background:
    Given student with "Ivan" firstName and "Ivanovich" lastName and "test@gmail.com" email exists

  Scenario: Assignee student to group
    Given  "AT-21-2" group exists
    When teacher assignee this student to "AT-21-2" group
    Then operation is successful
    And this student should have "AT-21-2" group

  Scenario: Create group
    Given "AT-21-2" group doesnt exist
    When "AT-21-2" group is created
    Then operation is successful
    And "AT-21-2" group should exist

  Scenario: Create group when group exist
    Given "AT-21-2" group exists
    When "AT-21-2" group is created
    Then operation should be finished with 409 "Group with name "AT-21-2" already exist" error

  Scenario: Get group
    Given "AT-21-2" group exists
    When system retrieve information about "AT-21-2" group
    Then operation is successful
    And group name should be "AT-21-2"
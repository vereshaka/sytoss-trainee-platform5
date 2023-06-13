Feature: Task Domain

  Background:
    Given teacher "Maksym" "Mitkov" exists
    And "SQL" discipline exists

  Scenario: system create a new task domain
    Given "First Domain" task domain doesnt exist
    When system create "First Domain" task domain
    Then operation is successful
    And "First Domain" task domain should be created

  Scenario: system not create a new task domain when it exist
    Given "First Domain" task domain exists
    When system create "First Domain" task domain when it exist
    Then operation should be finished with 409 "TaskDomain with name "First Domain" already exist" error

  Scenario: system get task domain by id
      Given "First Domain" task domain exists
      When system retrieve information about "First Domain" task domain
      Then operation is successful
      And system should been get "First Domain" information

  Scenario: system get not existing task domain by id
    Given "First Domain" task domain doesnt exist
    When system retrieve information about "First Domain" task domain
    Then operation should be finished with 404 "Task Domain with id "123" not found" error

  Scenario: Update task domain
    Given teacher "John" "Johnson" exists
    And "SQL" discipline exists for this teacher
    And "First Domain" task domain with "Fisrt Domain Script" script exists for this discipline
    When teacher updates "First Domain" task domain to "Second Domain"
    Then operation is successful
    And "Second Domain" task domain with "Fisrt Domain Script" script should be

  Scenario: Update task domain when task domain does not exist
    Given teacher "John" "Johnson" exists
    And "SQL" discipline exists for this teacher
    And "First Domain" task domain doesnt exist
    When teacher updates "First Domain" task domain to "Second Domain"
    Then operation should be finished with 404 "Task Domain with id "123" not found" error
Feature: Task Domain

  Scenario: system create a new task domain
    Given "First Domain" task domain doesnt exist
    When system create "First Domain" task domain
    Then operation is successful
    And "First Domain" task domain should be created

  Scenario: system not create a new task domain when it exist
    Given "First Domain" task domain exist
    When system create "First Domain" task domain when it exist
    Then operation should be finished with 409 "TaskDomain with name "First Domain" already exist" error
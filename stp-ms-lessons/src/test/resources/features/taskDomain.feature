Feature: Task Domain

  Scenario: system get task domain by id
      Given "First Domain" task domain exist
      When system retrieve information about "First Domain" task domain
      Then operation is successful
      And system should been get "First Domain" information

  Scenario: system get not existing task domain by id
    Given "First Domain" task domain does not exist
    When system retrieve information about "First Domain" task domain
    Then operation should be finished with 404 "Task Domain with id "123" not found" error
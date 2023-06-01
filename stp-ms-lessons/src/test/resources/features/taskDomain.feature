Feature: Task Domain

  Scenario: system get task domain by id
      Given "First Domain" task domain with "77" id exist
      When system try to find task domain by "77" id
      Then operation is successful
      And system should been get "First Domain" information
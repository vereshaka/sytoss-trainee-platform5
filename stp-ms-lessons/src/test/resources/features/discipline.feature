Feature: Discipline

  Scenario: teacher creates a new discipline
    Given teachers exist
      | firstName  | lastName  |
      | Maksym     | Mitkov    |
    And "SQL" discipline doesn't exist
    When teacher creates "SQL" discipline
    Then operation is successful

  Scenario: teacher creates a discipline that already exists
    Given teachers exist
      | firstName  | lastName  |
      | Maksym     | Mitkov    |
    And disciplines exist
      | discipline |
      | SQL        |
    When teacher creates "SQL" discipline
    Then operation should be finished with 409 "Discipline with name "SQL" already exist" error
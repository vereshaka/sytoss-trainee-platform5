Feature: Discipline

  Background:
    Given teacher "Maksym" "Mitkov" exists

  Scenario: teacher creates a new discipline
    Given "SQL" discipline doesn't exist
    When teacher creates "SQL" discipline
    Then operation is successful

  Scenario: teacher creates a discipline that already exists
    Given disciplines exist
      | discipline |
      | SQL        |
    When teacher creates "SQL" discipline
    Then operation should be finished with 409 "Discipline with name "SQL" already exist" error
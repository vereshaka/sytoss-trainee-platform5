Feature: Discipline

  Background:
    Given teacher "Maksym" "Mitkov" exists

  Scenario: teacher creates a new discipline
    Given discipline "SQL" doesn't exist
    When teacher creates "SQL" discipline
    Then operation is successful
    And "SQL" discipline should exist

  Scenario: teacher creates a discipline that already exists
    Given "SQL" discipline exists
    When teacher creates existing "SQL" discipline
    Then operation should be finished with 409 "Discipline with name "SQL" already exist" error

  Scenario: retrieve discipline information
    Given "SQL" discipline exists
    When receive "SQL" discipline information
    Then operation is successful
    And "SQL" discipline should be received
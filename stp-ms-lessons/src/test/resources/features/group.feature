Feature: Group

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: System retrieve all groups by discipline
    Given this teacher has "SQL" discipline with id *18 contains groups with id "11, 12"
    And this teacher has "Java" discipline with id *19 contains groups with id "13"
    When receive all groups by discipline with id *18
    Then operation is successful
    And groups of discipline with id *18 should be received
      | group |
      | 11    |
      | 12    |
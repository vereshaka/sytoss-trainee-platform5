Feature: Group

  Background:
    Given teacher "Maksym" "Mitkov" exists

  Scenario: system retrieve all groups by discipline
    Given groups exist
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | Java       | AT-13 |
      | SQL        | TT-12 |
    When receive all groups by "SQL" discipline
    Then operation is successful
    And groups should received
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | SQL        | TT-12 |
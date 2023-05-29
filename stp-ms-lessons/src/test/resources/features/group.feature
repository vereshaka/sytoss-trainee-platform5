Feature: Group

  Scenario: system retrieve all groups by discipline
    Given teachers exist
      | firstName  | lastName  |
      | Alexey     | Shatokhin |
    And groups exist
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
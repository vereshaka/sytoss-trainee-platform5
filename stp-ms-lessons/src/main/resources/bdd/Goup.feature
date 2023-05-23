Feature: Group

  Scenario: receive all groups by discipline
    Given groups exist
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | SQL        | TT-12 |
      | Java       | AT-13 |
    When receive all groups by "SQL" discipline
    Then operation is successful
    And groups should be sent
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | SQL        | TT-12 |
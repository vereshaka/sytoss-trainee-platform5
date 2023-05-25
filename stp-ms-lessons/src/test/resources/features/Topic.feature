Feature: Topic

  Scenario: system retrieve information list of topic
    Given topic exist
      | discipline | topic           |
      | SQL        | Join            |
      | SQL        | Join Inner      |
      | SQL        | Sorting results |
      | SQL        | Drop results    |
    When system retrieve information about topics by discipline
    Then operation is successful
    And should return
      | discipline | topic           |
      | SQL        | Join            |
      | SQL        | Join Inner      |
      | SQL        | Sorting results |
      | SQL        | Drop results    |
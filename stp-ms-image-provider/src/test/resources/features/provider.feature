Feature: Provider

  Scenario: get question image
    Given generated question image "What is Join?"
    When retrieve image by id 1
    Then operation is successful
    And image should be retrieved
Feature: Provider

  Scenario: get question image
    Given generated question image "What is Join?" with id *1
    When retrieve image by id *1
    Then operation is successful
    And image should be retrieved

  Scenario: get question image with wrong id
    Given question image "What is Join?" with id *1 doesnt exist
    When retrieve image by id *1
    Then operation should be finished with 404 error
Feature: Provider

  Background:
    Given generated question image "What is Join?"

  Scenario: get question image
    When retrieve image by id 1
    Then operation is successful
    And image should be retrieved

  Scenario: get question image with wrong id
    When retrieve image by id 3
    Then operation should be finished with 404 "Image with id "3" not found" error
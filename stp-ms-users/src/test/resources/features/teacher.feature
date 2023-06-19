Feature: Teacher

  Scenario: registration teacher
    Given teacher with "Ivan" firstName and "Ivanovich" lastName doesnt exist
    When anonymous register in system as teacher with "test@test.com" email and "Ivan" "Ivanovich" as name
    Then operation is successful
    And teacher with "Ivan" firstname and "Ivanovich" lastname should exist

  Scenario: update photo
    Given student with "Ivan" firstName and "Ivanovich" lastName and "test@gmail.com" email exists
    When this user upload the photo
    Then operation is successful
    And this user has photo

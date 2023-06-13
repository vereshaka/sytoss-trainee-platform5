Feature: Student

  Scenario: update photo
    Given student with "Ivan" firstName and "Ivanov" middleName and "Ivanovich" lastName and "test@gmail.com" email exists
    When student's photo is updated
    Then operation is successful
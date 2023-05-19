Scenario: Check student`s answer
  When user checks student's answer with checkResults button using answer,etalon, and databaseScript
  Then database should be created according to database script
  And answer and etalon should be written into the database
  And database should be dropped
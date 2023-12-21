Feature: check answer

  Scenario: Check correct student's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And etalon SQL is "select * from Discipline"
    And check SQL is "select * from Discipline"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""

  Scenario: Check wrong student's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And etalon SQL is "select * from Discipline"
    And check SQL is "select * from Topic"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0
    And Grade message is "Amount of data is different"

  Scenario: Check student's answer with condition
    Given Request contains database script as in "task-domain/script1.yml"
    And etalon SQL is "select * from Discipline ORDER BY id"
    And check SQL is "select * from Discipline"
    And answer should contains "ORDER BY" condition with "CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0.7
    And Grade message is ""ORDER BY" condition are failed to check"

  Scenario: Check etalon's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And check SQL is "select * from Discipline"
    When request sent to check etalon answer
    Then request should be processed successfully
    And should return that etalon is valid

  Scenario: Check not valid etalon's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And etalon SQL is "select * from Pages"
    When request sent to check etalon answer
    Then request should be processed successfully
    And should return that etalon is not valid

  Scenario: Check current сorrect student's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And check SQL is "select * from Discipline"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | ID | NAME  |
      | 1  | SQL   |
      | 2  | Mongo |

  Scenario: Check current incorrect student's answer
    Given Request contains database script as in "task-domain/script1.yml"
    And check SQL is "select * fr Discipline"
    When request sent to check incorrect script
    Then operation should be finished with "406" error

  Scenario: Check correct sequence of columns
    Given Request contains database script as in "task-domain/script1.yml"
    And check SQL is "select name,id from Discipline"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | NAME  | ID |
      | SQL   | 1  |
      | Mongo | 2  |

  Scenario: Check student's answer if result columns more than in teacher's answer result
    Given Request contains database script as in "task-domain/product_sale_script.yml"
    And etalon SQL is "SELECT Company, LName from Client ORDER BY Company DESC, LName"
    And check SQL is "SELECT * from Client ORDER BY Company DESC, LName"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1.0
    And Grade message is "There are more columns in the answer than in the etalon"

  Scenario: STP-XX Specify custom name for columns without name
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "select  sum(idclient), avg(idclient)   from Client c"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | C1 | C2 |
      | 28 | 4.0 |

  Scenario: STP-XX Duplicate column names
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "select  *    from Client c  inner join Sale s on c.idClient = s.idClient  where s.idClient = 4"
    When request sent to check
    Then request should be processed successfully
    And query result should be
      | IDCLIENT | LNAME    | FNAME  | MNAME    | COMPANY       | CITYCLIENT | PHONE         | IDSALE | IDCLIENT1 | IDPRODUCT | QUANTITY | DATESALE   |
      | 4        | Азаренко | Тетяна | Петрівна | ТОВ Відпустка | Львів      | +380505723577 | 6      | 4          | 3         | 5        | 2022-09-15 |

    @Bug
  Scenario: STP-791 GROUP BY exception
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "Select Company, sum(Quantity)   from Client c inner join Sale s on c.IdClient=s.IdProduct  group by c.IdClient "
    When request sent to check
    Then request should be processed with error 406

  Scenario: STP-791 GROUP BY correct
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "Select CompanY,c.Company, sum(Quantity)   from Client c inner join Sale s on c.IdClient=s.IdProduct  group by c.Company "
    When request sent to check
    Then request should be processed successfully

  Scenario: STP-791-test GROUP BY exception
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "select * from CLIENT group by COMPANY"
    When request sent to check
    Then request should be processed with error 406

  Scenario: STP-853 Check correct student's answer with in and sub-query
    Given Request contains database script as in "task-domain/sale.yml"
    And etalon SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale)"
    And check SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale)"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""

  Scenario: STP-853 Check correct student's answer with not in and sub-query
    Given Request contains database script as in "task-domain/sale.yml"
    And etalon SQL is "SELECT IdClient, LName FROM Client WHERE IdClient NOT IN (SELECT IdClient FROM Sale);"
    And check SQL is "SELECT IdClient, LName FROM Client WHERE IdClient NOT IN (SELECT IdClient FROM Sale);"
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""

  Scenario: STP-xxx Check line break
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is
    """
    Select CompanY,c.Company, sum(Quantity)
  from Client c inner join Sale s on c.IdClient=s.IdProduct
  group by c.Company
    """
    When request sent to check
    Then request should be processed successfully

  Scenario: STP-966 Check incorrect student's answer with with task conditions
    Given Request contains database script as in "task-domain/script_v1.yml"
    And etalon SQL is "SELECT DISTINCT C.IdClient, FName FROM Client C INNER JOIN Sale S ON C.IdClient= S.IdProduct"
    And check SQL is "Select distinct Client.IdClient, FName from Client LEFT JOIN Sale on Client.IdClient = Sale.IdClient where Sale.IdClient IS NOT NULL"
    And answer should contains "DISTINCT" condition with "CONTAINS" type
    And answer should contains "JOIN" condition with "CONTAINS" type
    And answer should contains "IN" condition with "NOT_CONTAINS" type
    And answer should contains "EXISTS" condition with "NOT_CONTAINS" type
    And answer should contains "NULL" condition with "NOT_CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0.7
    And Grade message is ""!NULL" condition are failed to check"


  @Bug
  Scenario: STP-975 GROUP BY grouping set
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is "Select idclient, idproduct, count(*) as cnt from sale group by grouping sets (idclient, idproduct) "
    When request sent to check
    Then request should be processed successfully

  @Bug
  Scenario: STP-975 GROUP BY grouping set #3
    Given Request contains database script from "task-domain/prod-trade23.yml" puml
    And check SQL is
    """
 SELECT IdClient, EXTRACT(year FROM DateSale) as YearClient, IdProduct, count(*) as QuantitySAle_Сделок
  FROM Sale
  group by GROUPING sets ((IdClient, EXTRACT(year FROM DateSale)), (IdProduct, IdClient))
    """
    When request sent to check
    Then request should be processed successfully

  Scenario: STP-981 Check correct student's answer with in condition
    Given Request contains database script as in "task-domain/sale.yml"
    And etalon SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale)"
    And check SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale)"
    And answer should contains "IN" condition with "CONTAINS" type
    And answer should contains "JOIN" condition with "NOT_CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""

  Scenario: STP-1011 Requiered commands aren't graded correctly - cause is spaces in required commands
    Given Request contains database script as in "task-domain/sale.yml"
    And multiline etalon SQL is:
    """
    select 1 as f, 2 as s, case
    when IdProduct >= 1
     then 'true'
     else 'false'
     end as result,
     NameProduct from Product where IdProduct IN (Select IdProduct from Sale)
  """
    And multiline check SQL is:
  """
    select 1 as f, 2 as b,
    case
    when IdProduct >= 1
     then 'true'
     else 'false'
     end as qq,
     NameProduct from Product where IdProduct IN (Select IdProduct from Sale)
  """
    And answer should contains "CASE " condition with "CONTAINS" type
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""

  Scenario: STP-1009-1 Sorting doesn't affect on check request result (sorting makes matter)
    Given Request contains database script as in "task-domain/sale.yml"
    And etalon SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale) order by IdProduct asc"
    And check SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale) order by IdProduct desc"
    And sorting is relevant
    When request coming to process
    Then request should be processed successfully
    And Grade value is 0.7
    And Grade message is "Sorting is wrong."

  Scenario: STP-1009-2 Sorting doesn't affect on check request result (sorting does not makes matter)
    Given Request contains database script as in "task-domain/sale.yml"
    And etalon SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale) order by IdProduct asc"
    And check SQL is "select IdProduct, NameProduct from Product where IdProduct IN (Select IdProduct from Sale) order by IdProduct desc"
    And sorting is not relevant
    When request coming to process
    Then request should be processed successfully
    And Grade value is 1
    And Grade message is ""
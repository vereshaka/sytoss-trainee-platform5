package cucumber;

import io.cucumber.java.en.Then;

import java.sql.Connection;
import java.sql.DriverManager;

public class ThenTest extends AbstractCucumberTest{
    @Then("database should be created according to database script")
    public void databaseShouldBeCreatedAccordingToDatabase(){
      /*  Class.forName("org.h2.Driver");
        String name = generateDatabaseName();
        url += name;
        Connection connection = DriverManager.getConnection(url, username, password);*/
    }

}

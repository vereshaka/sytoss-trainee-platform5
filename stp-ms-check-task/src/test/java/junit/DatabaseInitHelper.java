package junit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Scanner;

public class DatabaseInitHelper {

    protected String initDatabase() throws FileNotFoundException, URISyntaxException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("databaseTest.yml").toURI());
        FileReader fileReader = new FileReader(file);
        Scanner scan = new Scanner(fileReader);
        StringBuilder script = new StringBuilder();
        while (scan.hasNextLine()) {
            script.append(scan.nextLine());
        }
        return script.toString();
    }
}

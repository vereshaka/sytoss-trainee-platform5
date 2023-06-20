package com.sytoss.lessons.convertors;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PumlConvertorTest {

    @Test
    void convertToLiquibase() throws IOException {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script.yml");

        List<String> pumlScriptStrings = Arrays.stream(pumlConvertedScript.split("\n")).toList();
        List<String> liquibaseScriptStrings = Arrays.stream(liquibaseScript.split("\n")).toList();
        int quantityOfStrings = 0;

        for (int i = 0; i < pumlScriptStrings.size(); i++) {
            if (pumlScriptStrings.get(i).trim().equals(liquibaseScriptStrings.get(i).trim())) {
                quantityOfStrings++;
            }
        }

        assertEquals(quantityOfStrings, pumlScriptStrings.size());
    }

    @Test
    void convertToLiquibase2() throws IOException {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script.yml");
        assertEquals(liquibaseScript, pumlConvertedScript.trim());
    }

    private String readFromFile(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("script/" + path)).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        return String.join("\n", data);
    }
}
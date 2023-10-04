package com.sytoss.domain.bom.convertors;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PumlConvertorTest {

    @Test
    void convertToLiquibase() {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script_v1.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script_v1.yml");
        log.debug(pumlConvertedScript);
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
    void convertToLiquibase2() {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script_v1.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script_v1.yml");
        log.debug(pumlConvertedScript);
        assertEquals(liquibaseScript, pumlConvertedScript.trim());
    }
/*
    @Test
    void addLinksTest() {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script_v1.puml");
        List<String> entities = pumlConvertor.getEntities(pumlScript);
        String pumlConvertedScript = pumlConvertor.addLinks(String.join(StringUtils.LF + StringUtils.LF, entities), pumlScript, ConvertToPumlParameters.DB);
        String pumlExampleScript = readFromFile("puml/scriptWithLinks.puml");
        assertEquals(pumlExampleScript, pumlConvertedScript);
    }*/

    private String readFromFile(String path)  {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("script/" + path)).getFile());
        List<String> data = null;
        try {
            data = Files.readAllLines(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.join("\n", data);
    }
}
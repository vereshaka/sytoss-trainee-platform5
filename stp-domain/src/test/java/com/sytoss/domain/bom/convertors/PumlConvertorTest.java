package com.sytoss.domain.bom.convertors;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        String pumlScript = readFromFile("puml/script.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script.yml");

        System.out.println(pumlConvertedScript);
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
        String pumlScript = readFromFile("puml/script.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script.yml");
        System.out.println(pumlConvertedScript);
        assertEquals(liquibaseScript, pumlConvertedScript.trim());
    }

    @Test
    void addLinksTest() {
        PumlConvertor pumlConvertor = new PumlConvertor();
        String pumlScript = readFromFile("puml/script.puml");
        List<String> entities = pumlConvertor.getEntities(pumlScript);
        String pumlConvertedScript = pumlConvertor.addLinks(String.join(StringUtils.LF + StringUtils.LF, entities), pumlScript, ConvertToPumlParameters.DB);
        String pumlExampleScript = readFromFile("puml/scriptWithLinks.puml");
        assertEquals(pumlExampleScript, pumlConvertedScript);
    }

    @Test
    public void shouldCreateScript_v1(){
        PumlConvertor pumlConvertor = new PumlConvertor();
        String script = readFromFile("puml/script_v1.puml");
        String pumlScript = pumlConvertor.formatPuml(script);
        //log.info(pumlScript);
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        log.info(pumlConvertedScript);
    }

    @Test
    public void shouldParse(){
        PumlConvertor pumlConvertor = new PumlConvertor();
        String script = readFromFile("puml/script_v1.puml");
        List<Table> result = pumlConvertor.parse(script);
        //log.info(pumlScript);
    }


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
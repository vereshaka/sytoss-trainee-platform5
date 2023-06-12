package com.sytoss.lessons.convertors;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PumlConvertorTest {

    private final PumlConvertor pumlConvertor = new PumlConvertor();

    @Test
    void convertToLiquibase() throws IOException {
        String pumlScript = readFromFile("puml/script.puml");
        String pumlConvertedScript = pumlConvertor.convertToLiquibase(pumlScript);
        String liquibaseScript = readFromFile("liquibase/script.yml");
        assertEquals(liquibaseScript,pumlConvertedScript);
    }

    private String readFromFile(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("script/" + path)).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        return String.join("\n", data);
    }
}
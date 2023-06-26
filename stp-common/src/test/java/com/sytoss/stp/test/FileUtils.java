package com.sytoss.stp.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtils {

    private FileUtils() {

    }

    public static String readFromFile(String script) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("scripts/" + script).getFile());
        try {
            List<String> data = Files.readAllLines(Path.of(file.getPath()));
            return String.join("\n", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

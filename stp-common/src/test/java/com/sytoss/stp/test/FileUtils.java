package com.sytoss.stp.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class FileUtils {

    private FileUtils() {

    }

    public static String readFromFile(String script) {
        try {
            return IOUtils.toString(FileUtils.class.getResourceAsStream("/data/" + script), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Could not read script: " + script, e);
        }

    }
}

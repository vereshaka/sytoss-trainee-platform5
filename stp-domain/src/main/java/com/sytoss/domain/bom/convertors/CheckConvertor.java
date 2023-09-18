package com.sytoss.domain.bom.convertors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckConvertor {
    public static String formatTaskAnswer(String taskAnswer) {
        if (taskAnswer.matches(".+\\[.+].+")) {
            String newWherePart;
            Pattern pattern = Pattern.compile("where (\\S+) like '(.+)'", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(taskAnswer);
            while (matcher.find()) {
                String oldWherePart = matcher.group(0);
                String tableName = matcher.group(1);
                String condition = matcher.group(2).replaceAll("_", ".").replaceAll("%", ".+");

                if (!condition.matches(".+\\[[\\s\\d\\w]].+")) {
                    Pattern patternBoundsContent = Pattern.compile("\\[.]");
                    Matcher matcherBoundsContent = patternBoundsContent.matcher(condition);

                    String content;
                    String newContent = "";
                    while (matcherBoundsContent.find()) {
                        content = matcherBoundsContent.group(0);
                        newContent = "\\" + content.substring(1, content.length() - 1);
                    }
                    condition = condition.replaceAll(patternBoundsContent.pattern(), "\\" + newContent);
                }
                newWherePart = "where REGEXP_LIKE(" + tableName + ",'" + condition + "')";
                taskAnswer = taskAnswer.replace(oldWherePart, newWherePart);
                return taskAnswer;
            }
        }
        return taskAnswer;
    }
}

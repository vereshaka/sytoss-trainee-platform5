package com.sytoss.lessons.convertors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class PumlConvertor {
    private String indent = StringUtils.leftPad(" ",2);

    public String convertToLiquibase(String script) {
        List<String> entities = getEntities(script);
        StringBuilder createTableStringBuilder = createChangeSet();
        indent += StringUtils.leftPad(" ",2);
        List<String> entityCreateScript = entities.stream().map(this::convertPumlEntityToLiquibase).toList();
        String entityCreateScriptText = String.join("", entityCreateScript);
        createTableStringBuilder.append(entityCreateScriptText);

        return "databaseChangeLog:\n" + createTableStringBuilder;
    }

    private List<String> getEntities(String script) {
        Pattern pattern = Pattern.compile("entity.+\\{(?>\\n?.+)+\\n}");
        List<String> entities = new ArrayList<>();
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount(); j++) {
                entities.add(matcher.group(j));
            }
        }
        return entities;
    }

    private String getName(String entity) {
        Pattern pattern = Pattern.compile("\\S+");
        Matcher matcher = pattern.matcher(entity);
        List<String> allMatches = new ArrayList<>();
        for (int i = 0; i <= 1 && matcher.find(); i++) {
            allMatches.add(matcher.group());
        }
        return allMatches.get(1);
    }

    private Map<String, String> getParameters(String entity) {
        Pattern parametersPattern = Pattern.compile("\\w+:\\s?\\w+(\\s<<.+>>)?");
        Matcher matcher = parametersPattern.matcher(entity);
        List<String> groupsOfParameters = getGroups(matcher);
        Map<String, String> parameters = new LinkedHashMap<>();
        for (String group : groupsOfParameters) {
            List<String> values = Arrays.stream(group.split(":")).toList();
            values = values.stream().map(String::trim).toList();
            parameters.put(values.get(0), values.get(1));
        }
        return parameters;
    }

    private List<String> getGroups(Matcher matcher) {
        List<String> groups = new ArrayList<>();
        while (matcher.find()) {
            groups.add(matcher.group(0));
        }
        return groups;
    }

    private String convertPumlEntityToLiquibase(String entity) {
        String name = getName(entity);
        Map<String, String> parameters = getParameters(entity);
        return returnLiquibaseGroup(name, parameters);
    }

    private String returnLiquibaseGroup(String name, Map<String, String> parameters) {
        StringBuilder entity = new StringBuilder();
        String innerIndent = indent + StringUtils.leftPad(" ",4);
        entity.append(indent).append("- createTable:").append(StringUtils.LF)
                .append(innerIndent).append("tableName: ").append(name).append(StringUtils.LF)
                .append(innerIndent).append("columns:").append(StringUtils.LF);
        StringBuilder foreignKeyConstraintsStringBuilder = null;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            StringBuilder primaryKeyStringBuilder = null;
            if (value.matches("\\S+\\s<<.+>>")) {
                if (value.contains("PK")) {
                    primaryKeyStringBuilder = createPrimaryKey(indent + StringUtils.leftPad(" ",10));
                } else if (value.contains("FK")) {
                    Pattern pattern = Pattern.compile("<<\\S+\\s([A-z]+(\\([A-z]+\\)))>>");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()) {
                        String entityName2 = matcher.group(1);
                        String entityName2FieldName = matcher.group(2);
                        entityName2 = entityName2.replaceAll("\\(.+?\\)", "");
                        entityName2FieldName = entityName2FieldName.replaceAll("[()]", "");
                        if (foreignKeyConstraintsStringBuilder == null) {
                            foreignKeyConstraintsStringBuilder = new StringBuilder();
                        }
                        foreignKeyConstraintsStringBuilder.append(createForeignKey(name, key, entityName2, entityName2FieldName));
                    }
                }
                value = value.replaceAll("<<.+>>", "").trim();
            }
            String columnsIndent = innerIndent + "      ";
            entity.append(innerIndent).append(StringUtils.leftPad(" ",2)).append("- column:").append(StringUtils.LF)
                    .append(columnsIndent).append("name: ").append(key).append(StringUtils.LF)
                    .append(columnsIndent).append("type: ").append(value).append(StringUtils.LF);
            if (primaryKeyStringBuilder != null) {
                entity.append(columnsIndent).append(primaryKeyStringBuilder).append(StringUtils.LF);
            }
        }
        if (foreignKeyConstraintsStringBuilder != null) {
            entity.append(innerIndent).append("foreignKeyConstraints:").append(StringUtils.LF)
                    .append(innerIndent).append(foreignKeyConstraintsStringBuilder).append(StringUtils.LF);
        }
        return entity.toString();
    }

    private StringBuilder createChangeSet() {
        String innerIndent = indent + StringUtils.leftPad(" ",4);
        StringBuilder changeSet = new StringBuilder();
        changeSet.append(indent).append("- changeSet:").append(StringUtils.LF)
                .append(innerIndent).append("id: ").append("create-tables").append(StringUtils.LF)
                .append(innerIndent).append("author: ").append("compiled").append(StringUtils.LF)
                .append(innerIndent).append("changes:").append(StringUtils.LF);
        indent = innerIndent;
        return changeSet;
    }

    private StringBuilder createPrimaryKey(String indent) {
        return new StringBuilder().append("constraints:").append(StringUtils.LF).append(indent).append("  primaryKey: true");
    }

    private StringBuilder createForeignKey(String entityName1, String entityName1Field, String entityName2, String entityName2Field) {
        StringBuilder addForeignKey = new StringBuilder();
        String innerIndent = indent + StringUtils.leftPad(" ",10);

        addForeignKey.append(StringUtils.leftPad(" ",2)).append("- foreignKeyConstraint:").append(StringUtils.LF)
                .append(innerIndent).append("baseTableName: ").append(entityName1).append(StringUtils.LF)
                .append(innerIndent).append("baseColumnNames: ").append(entityName1Field).append(StringUtils.LF)
                .append(innerIndent).append("constraintName: ").append("fk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF)
                .append(innerIndent).append("referencedTableName: ").append(entityName2).append(StringUtils.LF)
                .append(innerIndent).append("referencedColumnNames: ").append(entityName2Field);
        return addForeignKey;
    }
}
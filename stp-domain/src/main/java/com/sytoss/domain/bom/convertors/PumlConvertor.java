package com.sytoss.domain.bom.convertors;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
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
    private final Pattern foreignKeyPattern = Pattern.compile("<<(\\S+\\s(([A-z]+)(\\([A-z]+\\))))>>");
    private String indent;

    public String convertToLiquibase(String script) {
        indent = StringUtils.leftPad(StringUtils.SPACE, 2);
        List<String> entities = getEntities(script);
        StringBuilder createTableStringBuilder = createChangeSet("create-tables");
        indent += StringUtils.leftPad(StringUtils.SPACE, 2);
        List<String> entityCreateScript = entities.stream().map(this::convertPumlEntityToLiquibase).toList();
        String entityCreateScriptText = String.join("", entityCreateScript);
        createTableStringBuilder.append(entityCreateScriptText);

        indent = StringUtils.leftPad(StringUtils.SPACE, 2);
        List<String> objects = getObjects(script);
        StringBuilder initTableStringBuilder = createChangeSet("init-tables");
        indent += StringUtils.leftPad(StringUtils.SPACE, 2);
        List<String> objectsCreateScript = objects.stream().map(this::convertPumlObjectToLiquibase).toList();
        String entityInitScriptText = String.join("", objectsCreateScript);
        initTableStringBuilder.append(entityInitScriptText);
        return "databaseChangeLog:\n" + createTableStringBuilder + initTableStringBuilder;
    }

    public List<String> getEntities(String script) {
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

    private String getName(String entity, int positionInScript) {
        Pattern pattern = Pattern.compile("\\S+");
        Matcher matcher = pattern.matcher(entity);
        List<String> allMatches = new ArrayList<>();
        for (int i = 0; i <= positionInScript && matcher.find(); i++) {
            allMatches.add(matcher.group());
        }
        return allMatches.get(positionInScript);
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
        String name = getName(entity, 1);
        Map<String, String> parameters = getParameters(entity);
        return returnCreateTableLiquibaseGroup(name, parameters);
    }

    private String returnCreateTableLiquibaseGroup(String name, Map<String, String> parameters) {
        StringBuilder entity = new StringBuilder();
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 4);
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
                    primaryKeyStringBuilder = createPrimaryKey(indent + StringUtils.leftPad(StringUtils.SPACE, 10));
                } else if (value.contains("FK")) {
                    Matcher matcher = foreignKeyPattern.matcher(value);
                    if (matcher.find()) {
                        String entityName2 = matcher.group(2);
                        String entityName2FieldName = matcher.group(4);
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
            entity.append(innerIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("- column:").append(StringUtils.LF)
                    .append(columnsIndent).append("name: ").append(key).append(StringUtils.LF)
                    .append(columnsIndent).append("type: ").append(value.toLowerCase(Locale.ENGLISH).equals("string") ? "varchar" : value).append(StringUtils.LF);
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

    private StringBuilder createChangeSet(String purpose) {
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 4);
        StringBuilder changeSet = new StringBuilder();
        changeSet.append(indent).append("- changeSet:").append(StringUtils.LF)
                .append(innerIndent).append("id: ").append(purpose).append(StringUtils.LF)
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
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 10);

        addForeignKey.append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("- foreignKeyConstraint:").append(StringUtils.LF)
                .append(innerIndent).append("baseTableName: ").append(entityName1).append(StringUtils.LF)
                .append(innerIndent).append("baseColumnNames: ").append(entityName1Field).append(StringUtils.LF)
                .append(innerIndent).append("constraintName: ").append("fk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF)
                .append(innerIndent).append("referencedTableName: ").append(entityName2).append(StringUtils.LF)
                .append(innerIndent).append("referencedColumnNames: ").append(entityName2Field);
        return addForeignKey;
    }

    public List<String> getObjects(String script) {
        Pattern pattern = Pattern.compile("object.+\\{(?>\\n?.+)+\\n}");
        List<String> objects = new ArrayList<>();
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount(); j++) {
                objects.add(matcher.group(j));
            }
        }
        return objects;
    }

    private String convertPumlObjectToLiquibase(String object) {
        String name = getName(object, 3);
        List<Map<String, String>> parameters = getObjectData(object);
        return returnInitTableLiquibaseGroup(name, parameters);
    }

    private List<Map<String, String>> getObjectData(String object) {
        Pattern parametersPattern = Pattern.compile("\\|.+\\|");
        Matcher matcher = parametersPattern.matcher(object);
        List<String> rows = getGroups(matcher);
        List<String> header = getRowValues(rows.get(0));
        List<Map<String, String>> valuesInTable = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).contains("|=")) {
                continue;
            }
            List<String> values = getRowValues(rows.get(i));
            values = values.stream().map(String::trim).toList();
            HashMap<String, String> valuesInRow = new LinkedHashMap<>();
            for (int j = 0; j < header.size(); j++) {
                valuesInRow.put(header.get(j), values.get(j));
            }
            valuesInTable.add(valuesInRow);
        }
        return valuesInTable;
    }

    private List<String> getRowValues(String row) {
        row = row.trim();
        Pattern pattern = Pattern.compile("\\|=?\\s[A-z0-9?\\s]+");
        Matcher matcher = pattern.matcher(row);
        List<String> allMatches = new ArrayList<>();
        while (matcher.find()) {
            String match = matcher.group();
            match = match.replaceAll("[|=]", "").trim();
            allMatches.add(match);
        }
        return allMatches;
    }

    private String returnInitTableLiquibaseGroup(String name, List<Map<String, String>> parameters) {
        StringBuilder object = new StringBuilder();
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 4);
        for (Map<String, String> map : parameters) {
            object.append(indent).append("- insert:").append(StringUtils.LF)
                    .append(innerIndent).append("columns:").append(StringUtils.LF);

            String columnsIndent = innerIndent + "      ";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                object.append(innerIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("- column:").append(StringUtils.LF)
                        .append(columnsIndent).append("name: ").append(entry.getKey()).append(StringUtils.LF)
                        .append(columnsIndent).append("value: ").append(entry.getValue()).append(StringUtils.LF);
            }
            object.append(innerIndent).append("tableName: ").append(name.substring(1)).append(StringUtils.LF);
        }

        return object.toString();
    }

    public String addLinks(String script, String mainScript, ConvertToPumlParameters convertToPumlParameters){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@startuml").append(StringUtils.LF).append(StringUtils.LF)
                     .append(script).append(StringUtils.LF).append(StringUtils.LF)
                     .append(initLinks(mainScript,convertToPumlParameters)).append(StringUtils.LF)
                     .append("@enduml");
        return stringBuilder.toString();
    }

    private String initLinks(String script, ConvertToPumlParameters convertToPumlParameters) {
        StringBuilder entityStringBuilder = new StringBuilder();
        StringBuilder dataStringBuilder = new StringBuilder();
        List<String> entities = getEntities(script);
        Matcher matcher;
        for (String entity : entities) {
            matcher = foreignKeyPattern.matcher(entity);
            if (matcher.find()) {
                String entityLink = convertToLink(entity, ConvertToPumlParameters.DB);
                String dataLink = convertToLink(entity, ConvertToPumlParameters.DATA);
                if (entityLink != null) {
                    entityStringBuilder.append(entityLink).append("\n");
                }
                if (dataLink != null) {
                    dataStringBuilder.append(dataLink).append("\n");
                }
            }
        }

        if (convertToPumlParameters.equals(ConvertToPumlParameters.DB)) {
            return entityStringBuilder.toString();
        } else if (convertToPumlParameters.equals(ConvertToPumlParameters.DATA)) {
            return dataStringBuilder.toString();
        } else {
            return entityStringBuilder.append("\n").append(dataStringBuilder).toString();
        }
    }

    private String convertToLink(String entity, ConvertToPumlParameters convertToPumlParameters) {
        Matcher matcher = foreignKeyPattern.matcher(entity);
        if (matcher.find()) {
            String name = getName(entity, 1);
            String match = matcher.group(3);
            if (convertToPumlParameters.equals(ConvertToPumlParameters.DATA)) {
                name = "d" + name;
                match = "d" + match;
            }
            return name + " --o " + match + " : " + matcher.group(1);
        }
        return null;
    }

    public String formatPum(String puml){
        Pattern pattern = Pattern.compile("data ([A-z]+)");
        Matcher matcher = pattern.matcher(puml);
        while (matcher.find()){
            String match = matcher.group(1);
            String newDataName = "object \"Data:"+match+"\" as d"+match;
            puml = puml.replaceAll(matcher.group(0),newDataName);
        }
        puml = puml.replaceAll("table", "entity");
        return puml;
    }
}
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
    private StringBuilder createTableStringBuilder;
    private StringBuilder initKeysStringBuilder;

    public String convertToLiquibase(String script) {
        List<String> entities = getEntities(script);
        List<String> entityCreateScript = entities.stream().map(this::convertPumlEntityToLiquibase).toList();
        String entityCreateScriptText = String.join("", entityCreateScript);
        StringBuilder databaseChangelogBuilder = new StringBuilder("databaseChangeLog:").append(StringUtils.LF);
        createTableStringBuilder = createChangeSet(entityCreateScriptText, "create-tables", "  ");
        initKeysStringBuilder = createChangeSet("", "init-keys", "\t");

        List<String> allLinks = getLinks(script);
        for (String link : allLinks) {
            createForeignKeys(link, "\t  ");
        }
        databaseChangelogBuilder.append(createTableStringBuilder).append(initKeysStringBuilder);
       // System.out.println(databaseChangelogBuilder);
        return databaseChangelogBuilder.toString();
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


    private String convertPumlEntityToLiquibase(String entity) {
        String name = getName(entity);
        Map<String, String> parameters = getParameters(entity);
        return returnLiquibaseGroup(name, parameters, "\t  ");
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
        Pattern parametersPattern = Pattern.compile("\\w+:\\s?\\w+");
        Matcher matcher = parametersPattern.matcher(entity);
        List<String> groupsOfParameters = getGroups(matcher);
        HashMap<String, String> parameters = new HashMap<>();
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

    private List<String> getLinks(String script) {
        Pattern allLinksPattern = Pattern.compile(".*o?--o?.*");
        Matcher matcher = allLinksPattern.matcher(script);
        List<String> allLinks = new ArrayList<>();
        while (matcher.find()) {
            allLinks.add(matcher.group(0));
        }
        return allLinks;
    }

    private String returnLiquibaseGroup(String name, Map<String, String> parameters, String indent) {
        StringBuilder entity = new StringBuilder();
        entity.append(indent).append("- createTable:").append(StringUtils.LF)
                .append(indent).append("\ttableName: ").append(name).append(StringUtils.LF)
                .append(indent).append("\tcolumns:").append(StringUtils.LF);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            entity.append(indent).append("\t  - column:").append(StringUtils.LF)
                    .append(indent).append("\t\t  name: ").append(entry.getKey()).append(StringUtils.LF)
                    .append(indent).append("\t\t  type: ").append(entry.getValue()).append(StringUtils.LF);
        }
        return entity.toString();
    }

    private StringBuilder createChangeSet(String script, String purpose, String indent) {
        StringBuilder changeSet = new StringBuilder();
        changeSet.append(indent).append("- changeSet:").append(StringUtils.LF)
                .append(indent).append("\tid: ").append(purpose).append(StringUtils.LF)
                .append(indent).append("\tauthor:").append("compiled").append(StringUtils.LF)
                .append(indent).append("\tchanges:").append(StringUtils.LF)
                .append(indent).append(script);

        return changeSet;
    }

    private String createEntity(String entityName1, String entityName2, String indent) {
        String name = entityName1 + "2" + entityName2;
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(entityName1 + "Id", "int");
        parameters.put(entityName2 + "Id", "int");
        return returnLiquibaseGroup(name, parameters, indent);
    }

    private void createForeignKeys(String script, String indent) {
        HashMap<String, String> data = getDataInLink(script);
        String wordLink = defineLinkType(data);
        data.put("wordLink", wordLink);
        String firstName = data.get("firstEntity");
        String secondName = data.get("secondEntity");
        if (wordLink.equals("OneToOne") || wordLink.equals("OneToMany")) {
            initKeysStringBuilder.append(createForeignKey(firstName, secondName, indent));
        } else if (wordLink.equals("ManyToOne")) {
            initKeysStringBuilder.append(createForeignKey(secondName, firstName, indent));
        } else {
            createTableStringBuilder.append(createEntity(firstName, secondName, indent));
            initKeysStringBuilder.append(createForeignKey(firstName, secondName, indent));
            initKeysStringBuilder.append(createForeignKey(secondName, firstName, indent));
            initKeysStringBuilder.append(createCompositePrimaryKey(secondName, firstName, indent));
        }

    }

    private StringBuilder createForeignKey(String entityName1, String entityName2, String indent) {
        StringBuilder addForeignKey = new StringBuilder();
        addForeignKey.append(indent).append("- addForeignKeyConstraint:").append(StringUtils.LF)
                .append(indent).append(indent).append("baseTableName: ").append(entityName1).append(StringUtils.LF)
                .append(indent).append(indent).append("baseColumnNames: ").append(entityName1).append("Id").append(StringUtils.LF)
                .append(indent).append(indent).append("constraintName: ").append("fk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF)
                .append(indent).append(indent).append("referencedTableName: ").append(entityName2).append(StringUtils.LF)
                .append(indent).append(indent).append("referencedColumnNames: ").append(entityName2).append("Id").append(StringUtils.LF);
        return addForeignKey;
    }

    private StringBuilder createCompositePrimaryKey(String entityName1, String entityName2, String indent) {
        StringBuilder addForeignKey = new StringBuilder();
        addForeignKey.append(indent).append("- addPrimaryKey:").append(StringUtils.LF)
                .append(indent).append(indent).append("tableName: ").append(entityName1).append("2").append(entityName2).append(StringUtils.LF)
                .append(indent).append(indent).append("columnNames: ").append(entityName1).append("Id").append(",").append(entityName1).append("Id").append(StringUtils.LF)
                .append(indent).append(indent).append("constraintName: ").append("pk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF);
        return addForeignKey;
    }

    private String defineLinkType(HashMap<String, String> data) {
        if (data.get("linkType").equals("o--") || data.get("linkType").equals("--o")) {
            String firstCardinals = data.get("cardinalsForFirst");
            String secondCardinals = data.get("cardinalsForSecond");
            if (firstCardinals == null && secondCardinals == null || Objects.requireNonNull(firstCardinals).contains("1")
                    && Objects.requireNonNull(secondCardinals).contains("1")) {
                return "OneToOne";
            } else if (firstCardinals.contains("1") && secondCardinals.contains("N")) {
                return "OneToMany";
            } else {
                return "ManyToOne";
            }
        } else {
            return "ManyToMany";
        }
    }

    private HashMap<String, String> getDataInLink(String script) {
        String signLinkType = getLinkType(script);
        HashMap<String, String> data = new HashMap<>();
        data.put("linkType", signLinkType);
        List<String> names = getNamesInLink(script);
        data.put("firstEntity", names.get(0));
        data.put("secondEntity", names.get(1));
        List<String> cardinals = getCardinalNumbers(script);
        data.put("cardinalsForFirst", cardinals.get(0));
        data.put("cardinalsForSecond", cardinals.get(1));
        return data;
    }

    private List<String> getNamesInLink(String link) {
        Pattern namesPattern = Pattern.compile("(^[A-Za-z]+|[A-Za-z]+$)");
        Matcher namesMatcher = namesPattern.matcher(link);
        List<String> names = new ArrayList<>();
        while (namesMatcher.find()) {
            names.add(namesMatcher.group(1));
        }
        return names;
    }

    private String getLinkType(String link) {
        Pattern linkTypePattern = Pattern.compile("o?--o?");
        Matcher linkTypeMatcher = linkTypePattern.matcher(link);
        if (linkTypeMatcher.find()) {
            return linkTypeMatcher.group(0);
        }
        return null;
    }

    private List<String> getCardinalNumbers(String link) {
        Pattern cardinalNumbersPattern = Pattern.compile("\"\\S+(\\s\\S+)?\"");
        Matcher cardinalNumbersMatcher = cardinalNumbersPattern.matcher(link);
        List<String> cardinalNumbers = new ArrayList<>();
        while (cardinalNumbersMatcher.find()) {
            cardinalNumbers.add(cardinalNumbersMatcher.group(0));
        }
        return cardinalNumbers;
    }
}

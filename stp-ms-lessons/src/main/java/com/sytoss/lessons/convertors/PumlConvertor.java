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
    private StringBuilder initKeysStringBuilder;

    private String indent = "  ";

    public String convertToLiquibase(String script) {
        //String indent = "  ";
        List<String> entities = getEntities(script);
        StringBuilder createTableStringBuilder = createChangeSet("create-tables");
        indent = indent + "  ";
        List<String> entityCreateScript = entities.stream().map(this::convertPumlEntityToLiquibase).toList();
        String entityCreateScriptText = String.join("", entityCreateScript);
        createTableStringBuilder.append(entityCreateScriptText);

        initKeysStringBuilder = createChangeSet("init-keys");

        List<String> allLinks = getLinks(script);
      /*  for (String link : allLinks) {
            createForeignKeys(link, "\t  ");
        }*/
        StringBuilder databaseChangelogBuilder = new StringBuilder("databaseChangeLog:").append(StringUtils.LF);
        databaseChangelogBuilder.append(createTableStringBuilder);
        System.out.println(databaseChangelogBuilder);
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
        String innerIndent = indent + "    ";
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
                    primaryKeyStringBuilder = createPrimaryKey(indent + "\t\t  ");
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
                value = value.replaceAll("<<.+>>", "");
            }
            String columnsIndent = innerIndent + "      ";
            entity.append(innerIndent).append("  ").append("- column:").append(StringUtils.LF)
                    .append(columnsIndent).append("name: ").append(key).append(StringUtils.LF)
                    .append(columnsIndent).append("type: ").append(value).append(StringUtils.LF);
            if (primaryKeyStringBuilder != null) {
                entity.append(columnsIndent).append(primaryKeyStringBuilder).append(StringUtils.LF);
            }
        }
        if (foreignKeyConstraintsStringBuilder != null) {
            entity.append(innerIndent).append("foreignKeyConstraints").append(StringUtils.LF)
                    .append(innerIndent).append(foreignKeyConstraintsStringBuilder).append(StringUtils.LF);
        }
        return entity.toString();
    }

    private StringBuilder createChangeSet(String purpose) {
        String innerIndent = indent + "    ";
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
        String innerIndent = indent + "          ";
        addForeignKey.append("  ").append("- foreignKeyConstraint:").append(StringUtils.LF)
                .append(innerIndent).append("baseTableName: ").append(entityName1).append(StringUtils.LF)
                .append(innerIndent).append("baseColumnNames: ").append(entityName1Field).append(StringUtils.LF)
                .append(innerIndent).append("constraintName: ").append("fk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF)
                .append(innerIndent).append("referencedTableName: ").append(entityName2).append(StringUtils.LF)
                .append(innerIndent).append("referencedColumnNames: ").append(entityName2Field);
        return addForeignKey;
    }

    private String defineLinkType(HashMap<String, String> data) {
        String link = data.get("linkType");
        List<String> links = List.of(link.substring(0, 1), link.substring(link.length() - 1, link.length() - 2));
        String left = "";
        String right = "";
        for (String part : links) {
            String res;
            if (part.matches("[{}]")) {
                res = "Many";
            } else {
                res = "One";
            }
            if (links.indexOf(part) == 0) {
                left = res;
            } else {
                right = res;
            }
        }
        return left + "To" + right;
    }

    private List<String> getLinks(String script) {
        Pattern allLinksPattern = Pattern.compile(".*--.*");
        Matcher matcher = allLinksPattern.matcher(script);
        List<String> allLinks = new ArrayList<>();
        while (matcher.find()) {
            allLinks.add(matcher.group(0));
        }
        return allLinks;
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
        Pattern namesPattern = Pattern.compile("[^\\s|{}o][A-z]+[^\\s|{}o]");
        Matcher namesMatcher = namesPattern.matcher(link);
        List<String> names = new ArrayList<>();
        while (namesMatcher.find()) {
            names.add(namesMatcher.group(0));
        }
        return names;
    }

    private String getLinkType(String link) {
        Pattern linkTypePattern = Pattern.compile("[|}o]+--[|{o]+");
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


    private void createForeignKeys(String script, String indent) {
        HashMap<String, String> data = getDataInLink(script);
        String wordLink = defineLinkType(data);
        // data.put("wordLink", wordLink);
        String firstName = data.get("firstEntity");
        String secondName = data.get("secondEntity");
        if (wordLink.equals("OneToOne") || wordLink.equals("OneToMany")) {
            // initKeysStringBuilder.append(createForeignKey(firstName, secondName, indent));
        } else if (wordLink.equals("ManyToOne")) {
            //  initKeysStringBuilder.append(createForeignKey(secondName, firstName, indent));
        } else {
            //  createTableStringBuilder.append(createEntity(firstName, secondName, indent));
            //  initKeysStringBuilder.append(createForeignKey(firstName, secondName, indent));
            //  initKeysStringBuilder.append(createForeignKey(secondName, firstName, indent));
            //   initKeysStringBuilder.append(createCompositePrimaryKey(secondName, firstName, indent));
        }

    }

   /* private String createEntity(String entityName1, String entityName2, String indent) {
        String name = entityName1 + "2" + entityName2;
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(entityName1 + "Id", "int");
        parameters.put(entityName2 + "Id", "int");
        return returnLiquibaseGroup(name, parameters);
    }*/
}

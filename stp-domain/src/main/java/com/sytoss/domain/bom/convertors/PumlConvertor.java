package com.sytoss.domain.bom.convertors;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.TaskDomainCouldNotCreateImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class PumlConvertor {

    private static final String NULL_VALUE_DISPLAY = "";
    private final Pattern foreignKeyPattern = Pattern.compile("<<(\\S+\\s(([A-z]+)\\s?(\\([A-z]+\\))))>>");

    public List<Table> parse(String pumlScript) {
        String boundary = UUID.randomUUID().toString();
        String workedScript = pumlScript
                .replaceAll("table", boundary + "table")
                .replaceAll("data", boundary + "data");
        List<String> items = Arrays.stream(workedScript.split(boundary)).filter(item -> !item.trim().isEmpty()).toList();
        List<String> tables = items.stream().filter(item -> item.trim().startsWith("table")).toList();
        List<String> datas = items.stream().filter(item -> item.trim().startsWith("data")).toList();
        List<Table> result = new ArrayList<>();
        tables.forEach(item -> result.add(parseTable(item)));
        datas.forEach(item -> parseData(item, result));
        validate(result);
        return result;
    }

    private void validate(List<Table> result) {
        //TODO: yevgenyv: add validation
    }

    private Table parseTable(String tableScript) {
        Table result = new Table();
        String[] definitions = tableScript.split("\\{");
        if (definitions.length != 2) {
            throw new RuntimeException("Unexpected format: #1");
        }
        result.setName(definitions[0].replaceAll("table", "").trim());
        List<String> lines = Arrays.stream(definitions[1].trim().replaceAll("}", "").split("\n")).filter(item -> !item.trim().isEmpty()).toList();
        lines.forEach(item -> result.getColumns().add(parseColumn(item)));
        return result;
    }

    private Column parseColumn(String columnDefinition) {
        String source = columnDefinition.trim().replaceAll("( )+", " ");
        Column result = new Column();
        String[] temp = source.split(":");
        result.setName(temp[0].trim());
        temp = temp[1].split("<<");
        result.setDatatype(temp[0].trim());
        for (int i = 1; i < temp.length; i++) {
            String additional = temp[i].replaceAll(">>", "").trim();
            if (additional.equals("PK")) {
                result.setPrimary(true);
                result.setNullable(false);
            } else if (additional.equals("NULLABLE")) {
                result.setNullable(true);
            } else if (additional.startsWith("FK")) {
                String[] dividedSpring = additional.substring(2).trim().split("\\(");
                result.setForeignKey(new ForeignKey());
                result.getForeignKey().setTargetTable(dividedSpring[0]);
                result.getForeignKey().setTargetColumn(dividedSpring[1].replaceAll("\\)", "").trim());
            }
        }
        return result;
    }

    private void parseData(String dataScript, List<Table> tables) {
        String[] definitions = dataScript.split("\\{");
        if (definitions.length != 2) {
            throw new RuntimeException("Unexpected format: #1");
        }
        String tableName = definitions[0].replaceAll("data", "").trim();
        Table result = tables.stream().filter(item -> item.getName().equalsIgnoreCase(tableName)).findFirst().orElse(null);
        if (result == null) {
            throw new RuntimeException("Table not found for data. Table name: " + tableName);
        }
        List<String> lines = Arrays.stream(definitions[1].trim().split("\n")).filter(item -> !item.trim().isEmpty() && !item.trim().equals("}")).toList();
        List<String> headers = Arrays.stream(lines.get(0).replaceAll("=", "").split("\\|")).filter(item -> item.trim().length() > 0).toList();
        for (int i = 1; i < lines.size(); i++) {
            String rawString = lines.get(i).trim();
            String source = rawString;
            if (source.startsWith("|")) {
                source = source.substring(1);
            }
            if (source.endsWith("|")) {
                source = source.substring(0, source.length() - 1);
            }
            List<String> values = List.of(source.split("\\|"));
            DataRow row = new DataRow();
            row.setRaw(rawString);
            if (values.size() > headers.size()) {
                throw new RuntimeException("Wrong amount of columns. \n" + lines.get(0) + "\n" + rawString);
            }
            for (int j = 0; j < values.size(); j++) {
                row.getValues().put(headers.get(j).trim(), values.get(j).trim());
            }
            result.getRows().add(row);
        }
    }

    public String convertToLiquibase(String script) {
        List<Table> tables = parse(script);

        StringBuilder createTableStringBuilder = createChangeSet("create-tables");
        StringBuilder initTableStringBuilder = createChangeSet("init-tables");
        StringBuilder foreignKeyStringBuilder = createChangeSet("init-foreign-keys");

        List<String> createTableScripts = tables.stream().map(this::returnCreateTableLiquibaseGroup).toList();
        List<String> initTableScripts = tables.stream().map(this::returnInitTableLiquibaseGroup).toList();
        List<String> initForeignKeysScripts = tables.stream().map(this::returnInitForeignKeys).toList();

        createTableStringBuilder.append(String.join("", createTableScripts));
        initTableStringBuilder.append(String.join("", initTableScripts));
        foreignKeyStringBuilder.append(String.join("", initForeignKeysScripts));
        return "databaseChangeLog:\n" + createTableStringBuilder + initTableStringBuilder + foreignKeyStringBuilder;
    }

    private String returnCreateTableLiquibaseGroup(Table table) {
        StringBuilder entity = new StringBuilder();
        String indent = StringUtils.leftPad(StringUtils.SPACE, 8);
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 4);
        String columnsIndent = innerIndent + StringUtils.leftPad(StringUtils.SPACE, 6);
        entity.append(indent).append("- createTable:").append(StringUtils.LF)
                .append(innerIndent).append("tableName: ").append(table.getName()).append(StringUtils.LF)
                .append(innerIndent).append("columns:").append(StringUtils.LF);
        for (Column column : table.getColumns()) {
            String name = column.getName();
            String datatype = column.getDatatype();
            StringBuilder constraintStringBuilder = null;
            if (column.isPrimary() || column.isNullable()) {
                constraintStringBuilder = new StringBuilder();
                constraintStringBuilder.append("constraints:").append(StringUtils.LF);
            }
            if (column.isPrimary()) {
                constraintStringBuilder.append(columnsIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("primaryKey: true");
            }
            if (column.isNullable()) {
                if(column.isPrimary()){
                    constraintStringBuilder.append(StringUtils.LF);
                }
                constraintStringBuilder.append(columnsIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("nullable: true");
            }

            entity.append(innerIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("- column:").append(StringUtils.LF)
                    .append(columnsIndent).append("name: ").append(name).append(StringUtils.LF)
                    .append(columnsIndent).append("type: ").append(datatype.toLowerCase(Locale.ENGLISH).equals("string") ? "varchar" : datatype).append(StringUtils.LF);
            if (constraintStringBuilder != null) {
                entity.append(columnsIndent).append(constraintStringBuilder).append(StringUtils.LF);
            }
        }
        return entity.toString();
    }

    private String returnInitTableLiquibaseGroup(Table table) {
        StringBuilder object = new StringBuilder();
        String indent = StringUtils.leftPad(StringUtils.SPACE, 8);
        String innerIndent = indent + StringUtils.leftPad(StringUtils.SPACE, 4);
        for (DataRow dataRow : table.getRows()) {
            object.append(indent).append("- insert:").append(StringUtils.LF)
                    .append(innerIndent).append("columns:").append(StringUtils.LF);

            String columnsIndent = innerIndent + "      ";
            for (Map.Entry<String, String> rows : dataRow.getValues().entrySet()) {
                object.append(innerIndent).append(StringUtils.leftPad(StringUtils.SPACE, 2)).append("- column:").append(StringUtils.LF)
                        .append(columnsIndent).append("name: ").append(rows.getKey()).append(StringUtils.LF);
                Column currentColumn = table.getColumns().get(table.getColumns().stream().map(el->el.getName().toUpperCase()).toList().indexOf(rows.getKey().toUpperCase()));
                if (currentColumn.isBoolean()) {
                    object.append(columnsIndent).append("valueBoolean: ").append(rows.getValue()).append(StringUtils.LF);
                } else if (currentColumn.isDate()) {
                    if(rows.getValue().matches("\\d{4}-\\d{2}-\\d{2}")){
                        object.append(columnsIndent).append("valueComputed: CAST(N'").append(rows.getValue()).append("' as DateTime)").append(StringUtils.LF);
                    }else if(rows.getValue().matches("\\d{2}.\\d{2}.\\d{4}")){
                        String[] dates = rows.getValue().split("\\.");
                        object.append(columnsIndent).append("valueComputed: CAST(N'").append(dates[2]).append("-")
                                .append(dates[1]).append("-")
                                .append(dates[0]).append("' as DateTime)").append(StringUtils.LF);
                    }


                } else if (currentColumn.isNumber()) {
                    object.append(columnsIndent).append("valueNumeric: ").append(rows.getValue()).append(StringUtils.LF);
                } else {
                    object.append(columnsIndent).append("value: ").append(rows.getValue().equals("") ? "null" : rows.getValue()).append(StringUtils.LF);
                }

            }
            object.append(innerIndent).append("tableName: ").append(table.getName()).append(StringUtils.LF);
        }

        return object.toString();
    }

    private String returnInitForeignKeys(Table table) {
        StringBuilder foreignKeyConstraintsStringBuilder = new StringBuilder();
        for (Column column : table.getColumns()) {
            if (column.getForeignKey() != null) {
                foreignKeyConstraintsStringBuilder.append(createForeignKey(table.getName(), column.getName(), column.getForeignKey().getTargetTable(), column.getForeignKey().getTargetColumn())).append("\n");
            }
        }
        return foreignKeyConstraintsStringBuilder.toString();
    }

    private StringBuilder createForeignKey(String entityName1, String entityName1Field, String entityName2, String entityName2Field) {
        StringBuilder addForeignKey = new StringBuilder();
        String innerIndent = StringUtils.leftPad(StringUtils.SPACE, 12);

        addForeignKey.append(StringUtils.leftPad(StringUtils.SPACE, 8)).append("- addForeignKeyConstraint:").append(StringUtils.LF)
                .append(innerIndent).append("baseTableName: ").append(entityName1).append(StringUtils.LF)
                .append(innerIndent).append("baseColumnNames: ").append(entityName1Field).append(StringUtils.LF)
                .append(innerIndent).append("constraintName: ").append("fk_").append(entityName1.toLowerCase()).append("_2_").append(entityName2.toLowerCase()).append(StringUtils.LF)
                .append(innerIndent).append("referencedTableName: ").append(entityName2).append(StringUtils.LF)
                .append(innerIndent).append("referencedColumnNames: ").append(entityName2Field);
        return addForeignKey;
    }

    public List<String> getEntities(String script) {
        Pattern pattern = Pattern.compile("entity.+\\{\\n?(?>\\n?.+)+\\n?\\n}");
        List<String> entities = new ArrayList<>();
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount(); j++) {
                entities.add(matcher.group(j).replaceAll("(?m)^[ \\t]*\\r?\\n", ""));
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

    private StringBuilder createChangeSet(String purpose) {
        String indent = StringUtils.leftPad(StringUtils.SPACE, 2);
        String innerIndent = StringUtils.leftPad(StringUtils.SPACE, 6);
        StringBuilder changeSet = new StringBuilder();
        changeSet.append(indent).append("- changeSet:").append(StringUtils.LF)
                .append(innerIndent).append("id: ").append(purpose).append(StringUtils.LF)
                .append(innerIndent).append("author: ").append("compiled").append(StringUtils.LF)
                .append(innerIndent).append("changes:").append(StringUtils.LF);
        return changeSet;
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

    public String addLinks(String script, String mainScript, ConvertToPumlParameters convertToPumlParameters) {
        String stringBuilder = "@startuml" + StringUtils.LF + StringUtils.LF +
                script + StringUtils.LF + StringUtils.LF +
                initLinks(mainScript, convertToPumlParameters) + StringUtils.LF +
                "@enduml";
        return stringBuilder;
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

    public String formatPuml(String puml) {
        Pattern pattern = Pattern.compile("data ([A-z]+)");
        Matcher matcher = pattern.matcher(puml);
        while (matcher.find()) {
            String match = matcher.group(1);
            String newDataName = "object \"Data:" + match + "\" as d" + match;
            puml = puml.replaceAll(matcher.group(0), newDataName);
        }
        puml = puml.replaceAll("table", "entity").replaceAll("undefined","");
        List<String> pumlParts = Arrays.stream(puml.split("\n")).toList().stream().filter(el->!el.equals("")).toList();
        puml = String.join("\n",pumlParts);
        puml = puml.replaceAll("entity","\nentity").replaceAll("object","\nobject");
        return puml;
    }

    public byte[] generatePngFromPuml(String puml, ConvertToPumlParameters convertToPumlParameters) {
        if (puml == null) {
            return getClass().getClassLoader().getResource("plantUMLBlank.jpg").getFile().getBytes();
        }
        puml = formatPuml(puml);
        List<String> objects = new ArrayList<>();
        if (convertToPumlParameters.equals(ConvertToPumlParameters.DB)) {
            objects = getEntities(puml);
        } else if (convertToPumlParameters.equals(ConvertToPumlParameters.DATA)) {
            objects = getObjects(puml);
        }

        String pumlConvertedScript = addLinks(String.join(StringUtils.LF + StringUtils.LF, objects), puml, convertToPumlParameters);
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        try {
            SourceStringReader reader = new SourceStringReader(pumlConvertedScript);
            String result = reader.outputImage(png).getDescription();

            File imageFile = File.createTempFile("img", ".png");
            ByteArrayInputStream bis = new ByteArrayInputStream(png.toByteArray());
            BufferedImage bufferedImage = ImageIO.read(bis);
            ImageIO.write(bufferedImage, "png", imageFile);

            if (!result.isEmpty()) {
                return png.toByteArray();
            }
        } catch (Exception e) {
            throw new TaskDomainCouldNotCreateImageException();
        }
        return null;
    }
}
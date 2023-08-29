package com.sytoss.checktask.stp.bdd.common;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.checktask.QueryResult;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import liquibase.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTableCommon extends CheckTaskIntegrationTest {

    @DataTableType
    public QueryResult mapQueryResult(DataTable table) {
        List<Map<String, String>> tableMaps = table.entries();
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        for (Map<String, String> tableMap : tableMaps) {
            Map<String, Object> resultMap = new HashMap<>();
            for (String key : tableMap.keySet().stream().toList()) {
                resultMap.put(key, StringUtil.isNumeric(tableMap.get(key)) ? Integer.parseInt(tableMap.get(key)) : tableMap.get(key));
            }
            resultMaps.add(resultMap);
        }
        QueryResult queryResult = new QueryResult();

        List<String> header = tableMaps.get(0).keySet().stream().toList();
        queryResult.setHeader(header);
        for (Map<String, Object> row : resultMaps) {
            queryResult.addValues(row);
        }

        return queryResult;
    }
}

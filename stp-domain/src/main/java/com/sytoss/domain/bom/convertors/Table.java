package com.sytoss.domain.bom.convertors;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Table {

    private String name;

    private List<Column> columns = new ArrayList<>();

    private List<DataRow> rows = new ArrayList<>();

}

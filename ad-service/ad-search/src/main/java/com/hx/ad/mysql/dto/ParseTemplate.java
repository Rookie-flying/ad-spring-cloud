package com.hx.ad.mysql.dto;

import com.hx.ad.mysql.constant.OpType;
import lombok.Data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 解析模板对象，并装载到 tableTemplateMap 中，建立 <表名,TableTemplate>的关系
 */
@Data
public class ParseTemplate {

    private String database;

    private Map<String, TableTemplate>  tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template _template) {

        ParseTemplate template = new ParseTemplate();
        template.setDatabase(_template.getDatabase());

        for (JsonTable table: _template.getTableList()) {

            String name = table.getTableName();
            Integer level = table.getLevel();

            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            tableTemplate.setLevel(level.toString());
            template.tableTemplateMap.put(name, tableTemplate);

            //遍历操作类型对应得列
            Map<OpType, List<String>> opTypeListMap =
                    tableTemplate.getOpTypeFieldSetMap();

            for (JsonTable.Column column : table.getInsert()) {

                // 如果Map中不存在ADD得List,则创建List，并且保存进Map中，然后将insert的值放入List中
                getAndCreateIfNeed(
                        OpType.ADD,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            for (JsonTable.Column column : table.getUpdate()) {

                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            for (JsonTable.Column column : table.getDelete()) {

                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
        }

        return template;
    }

    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map,
                                               Supplier<R> factory) {

        return map.computeIfAbsent(key, k->factory.get());
    }
}

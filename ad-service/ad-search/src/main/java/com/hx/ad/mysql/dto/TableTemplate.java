package com.hx.ad.mysql.dto;

import com.hx.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    private Map<OpType, List<String> > opTypeFieldSetMap = new HashMap<>();

    /*
     * 字段索引 -> 字段名
     */
    private Map<Integer, String> postMap = new HashMap<>();
}

package com.hx.ad.mysql.dto;

import com.hx.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简化BinlogRowData格式
 */
@Data
public class MySqlRowData {

    private String tableName;

    private String level;

    private OpType opType;

    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}

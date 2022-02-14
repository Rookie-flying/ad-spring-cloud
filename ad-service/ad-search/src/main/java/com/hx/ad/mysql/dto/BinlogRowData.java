package com.hx.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BinlogRowData {

    private TableTemplate table;

    private EventType eventType;

    private List<Map<String ,String>> after;

    //这个字段为了配合updateEvent只是列举出来，但我们只需要关注after
    private List<Map<String, String>> before;
}

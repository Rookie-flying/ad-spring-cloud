package com.hx.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.hx.ad.mysql.constant.Constant;
import com.hx.ad.mysql.constant.OpType;
import com.hx.ad.mysql.dto.BinlogRowData;
import com.hx.ad.mysql.dto.MySqlRowData;
import com.hx.ad.mysql.dto.TableTemplate;
import com.hx.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements IListener{

    //@Resource按照名字注入
    @Resource(name = "")
    private ISender sender;

    private final AggregationListener aggregationListener;

    //@Autowired按照类型注入
    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {

        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((k, v) ->
        aggregationListener.register(v, k, this));
    }

    /**
     * 将BinlogRowData 转化为 MysqlRowData
     * @param eventData
     */
    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        //包装成最后需要投递的数据格式
        MySqlRowData rowData = new MySqlRowData();

        rowData.setTableName(table.getTableName());
        rowData.setLevel(table.getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        for (Map<String, String> afterMap : eventData.getAfter()) {

            Map<String, String> _afterMap = new HashMap<>();

            for (Map.Entry<String, String> entry : afterMap.entrySet()) {

                String colName = entry.getKey();
                String colValue = entry.getValue();

                _afterMap.put(colName, colValue);
            }

            rowData.getFieldValueMap().add(_afterMap);
        }

        sender.sender(rowData);
    }
}

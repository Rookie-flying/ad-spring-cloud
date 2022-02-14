package com.hx.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.jmx.BinaryLogClientMXBean;
import com.hx.ad.mysql.TemplateHolder;
import com.hx.ad.mysql.dto.BinlogRowData;
import com.hx.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    private Map<String, IListener> listenerMap = new HashMap<>();

    private final TemplateHolder templateHolder;


    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    private String genKey(String dbName, String tableName) {

        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName,
                         IListener iListener) {

        log.info("register : {}-{}", _dbName, _tableName);
        this.listenerMap.put(genKey(_dbName, _tableName), iListener);
    }


    /**
     * TABLE_MAP_EVENT用于描述即将发生变化的表的结构。
     * 当用户提交一条修改语句时(如, insert, update, delete)，MySQL会产生2个Binlog事件:
     * 第一个就是TABLE_MAP_EVENT，用于描述改变对应表的结构(表名, 列的数据类型等信息)；
     * 紧接着的是ROWS_EVENT，用于描述对应表的行的变化值，后续会接着介绍。
     * @param event
     */
    @Override
    public void onEvent(Event event) {

        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);

        // 当表即将发生事件时，记录该表的表名和数据库名
        if (type == EventType.TABLE_MAP) {

            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        if (type != EventType.EXT_UPDATE_ROWS
                && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS) {
            return;
        }

        // 表名和库名是否已经填充
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }

        //找出对应表有兴趣的监听器
        String key = genKey(this.dbName, this.tableName);
        IListener iListener = this.listenerMap.get(key);

        if (null == listenerMap) {
            log.debug("skip {}", key);
            return;
        }

        log.info("trigger event: {}", type.name());

        try {

            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }

            rowData.setEventType(type);
            iListener.onEvent(rowData);

        } catch (Exception ex) {

            ex.printStackTrace();
            log.error(ex.getMessage());
        } finally {
            //该事件处理完毕
            this.dbName = "";
            this.tableName = "";
        }
    }

    /**
     * 将eventData转换为update，write，delete类型的Data，并得到它们的row数据
     * @param eventData
     * @return
     */
    private List<Serializable[]> getAfterValues(EventData eventData) {

        if (eventData instanceof WriteRowsEventData) {

            return ((WriteRowsEventData) eventData).getRows();
        }

        //udpateRows 有before 和 after两个List，所以取出after即可
        if (eventData instanceof UpdateRowsEventData) {

            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry :: getValue)
                    .collect(Collectors.toList());
        }

        if (eventData instanceof DeleteRowsEventData) {

            return ((DeleteRowsEventData) eventData).getRows();
        }

        return Collections.emptyList();
    }

    private BinlogRowData buildRowData (EventData eventData) {

        TableTemplate table = templateHolder.getTable(tableName);

        if (null == table) {

            log.warn("table {} not found", tableName);
            return null;
        }

        //列名->值
        List<Map<String, String>> afterMapList = new ArrayList<>();

        for (Serializable[] after : getAfterValues(eventData)) {

            Map<String, String> afterMap = new HashMap<>();

            int colLen = after.length;

            for (int ix = 0 ; ix < colLen ; ++ix) {

                //取出当前位置对应的列名
                String colName = table.getPostMap().get(ix);

                //如果没有则说明不关心这个列
                if (null == colName) {
                    log.debug("ignore position: {}", ix);
                    continue;
                }

                String colValue = after[ix].toString();
                afterMap.put(colName, colValue);
            }

            afterMapList.add(afterMap);
        }

        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(table);

        return rowData;
    }
}

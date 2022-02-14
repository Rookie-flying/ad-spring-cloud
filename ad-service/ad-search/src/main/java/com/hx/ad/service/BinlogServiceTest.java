package com.hx.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

/*
write-----------------
WriteRowsEventData{tableId=98, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
    [10, 10, plan, 1, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019]
]}

Tue Jan 01 08:00:00 CST 2019 需要解析成 2019-01-01 00:00:00
* */
public class BinlogServiceTest {

    public static void main(String[] args) throws Exception {
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "root"
        );

        client.registerEventListener(event -> {

            EventData data = event.getData();

            if (data instanceof UpdateRowsEventData) {

                System.out.println("update----------------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {

                System.out.println("write-----------------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {

                System.out.println("delete-----------------");
                System.out.println(data.toString());
            }
        });

        client.connect();
    }
}

package com.hx.ad.mysql.listener;

import com.hx.ad.mysql.dto.BinlogRowData;

public interface IListener {

    void register();
    void onEvent(BinlogRowData eventData);
}

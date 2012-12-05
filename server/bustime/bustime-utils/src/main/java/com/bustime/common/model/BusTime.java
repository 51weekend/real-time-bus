/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-5.
 */

package com.bustime.common.model;

import com.alibaba.fastjson.JSON;

/**
 * TODO.
 *
 * @author chengdong
 */
public class BusTime {
    private String ftime;
    private String dataGuid;

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getDataGuid() {
        return dataGuid;
    }

    public void setDataGuid(String dataGuid) {
        this.dataGuid = dataGuid;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

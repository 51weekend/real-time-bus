/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.common.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.alibaba.fastjson.JSON;

/**
 * TODO.
 *
 * @author chengdong
 */
public class StationBus {

    private String lineGuid;
    private String link;
    private String standCode;
    // 下面三个字段不持久化
    private String busNumber;
    private String standNum;
    private String time;

    // private String lineInfo;
    // private String trend;

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getLineGuid() {
        return lineGuid;
    }

    public void setLineGuid(String lineGuid) {
        this.lineGuid = lineGuid;
    }

    @JsonIgnore
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStandNum() {
        return standNum;
    }

    public void setStandNum(String standNum) {
        this.standNum = standNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStandCode() {
        return standCode;
    }

    public void setStandCode(String standCode) {
        this.standCode = standCode;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

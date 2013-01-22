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

    private String lineInfo;
    private String lineNumber;
    private String startStation;
    private String endStation;

    // private String trend;

    @JsonIgnore
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
        if ("进站".equals(standNum)) {
            this.standNum = "即将进站";
        } else {
            this.standNum = standNum;
        }
    }

    @JsonIgnore
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonIgnore
    public String getStandCode() {
        return standCode;
    }

    public void setStandCode(String standCode) {
        this.standCode = standCode;
    }

    @JsonIgnore
    public String getLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(String lineInfo) {
        if (lineInfo != null) {
            this.lineNumber = lineInfo.split("\\(")[0];
        }
        this.lineInfo = lineInfo;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        if (lineNumber != null) {
            this.lineNumber = lineNumber.split("\\(")[0];
            return;
        }
        this.lineNumber = lineNumber;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

}

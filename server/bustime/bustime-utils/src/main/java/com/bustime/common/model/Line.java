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
public class Line {

    private String lineGuid;
    private String lineNumber;
    private String lineInfo;
    private String link;
    private String trend;
    private int totalStation;
    private String runTime;
    private String startStation;
    private String endStation;

    public String getLineGuid() {
        return lineGuid;
    }

    public void setLineGuid(String lineGuid) {
        this.lineGuid = lineGuid;
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

    @JsonIgnore
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonIgnore
    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        if (trend != null) {
            this.trend = trend.replace("=&gt;", "->");
            if (setStartAndEnd("->")) {
                return;
            }
            if (setStartAndEnd("-")) {
                return;
            }
            if (setStartAndEnd("—")) {
                return;
            }

            this.startStation = trend;

        }
    }

    private boolean setStartAndEnd(String split) {
        String[] startAndEnd = this.trend.split(split);
        if (startAndEnd.length > 1) {
            this.startStation = startAndEnd[0];
            this.endStation = startAndEnd[1];
            return true;
        }
        return false;
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

    public int getTotalStation() {
        return totalStation;
    }

    public void setTotalStation(int totalStation) {
        this.totalStation = totalStation;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        if (endStation == null) {
            return "";
        }
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

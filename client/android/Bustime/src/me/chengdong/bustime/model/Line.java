/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package me.chengdong.bustime.model;

import org.json.JSONObject;

/**
 * TODO.
 *
 * @author chengdong
 */
public class Line {

    private String lineGuid;
    private String lineNumber;
    private String lineInfo;
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

    public String getLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(String lineInfo) {
        this.lineInfo = lineInfo;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
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
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public void deserialize(JSONObject json) {
        lineNumber = json.optString("lineNumber", "");
        lineGuid = json.optString("lineGuid", "");
        lineInfo = json.optString("lineInfo", "");
        startStation = json.optString("startStation", "");
        endStation = json.optString("endStation", "");
        totalStation = json.optInt("totalStation", 0);
    }

}

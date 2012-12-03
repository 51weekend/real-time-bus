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
public class StationBus {

    private String lineGuid;
    private String standNum;
    private String time;
    private String lineNumber;

    public String getLineGuid() {
        return lineGuid;
    }

    public void setLineGuid(String lineGuid) {
        this.lineGuid = lineGuid;
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

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void deserialize(JSONObject json) {
        lineGuid = json.optString("lineGuid", "");
        standNum = json.optString("standNum", "");
        time = json.optString("time", "");
        lineNumber = json.optString("lineNumber", "");
    }

}

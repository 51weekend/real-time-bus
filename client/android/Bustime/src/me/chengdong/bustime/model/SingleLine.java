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
public class SingleLine {
    private String lineGuid;
    private String standCode;
    private String standName;
    private String time;

    public String getLineGuid() {
        return lineGuid;
    }

    public void setLineGuid(String lineGuid) {
        this.lineGuid = lineGuid;
    }

    public String getStandCode() {
        return standCode;
    }

    public void setStandCode(String standCode) {
        this.standCode = standCode;
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void deserialize(JSONObject json) {
        this.standCode = json.optString("standCode");
        this.standName = json.optString("standName");
        this.time = json.optString("time");
    }

}

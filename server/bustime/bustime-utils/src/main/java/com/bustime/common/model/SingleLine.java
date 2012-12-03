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
public class SingleLine {
    private String standCode;
    private String standName;
    private String link;
    private String time;
    private String lineGuid;

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

    @JsonIgnore
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        if (time == null) {
            return "";
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonIgnore
    public String getLineGuid() {
        return lineGuid;
    }

    public void setLineGuid(String lineGuid) {
        this.lineGuid = lineGuid;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.core.model;

import com.alibaba.fastjson.JSON;

/**
 * TODO.
 *
 * @author chengdong
 */
public class Line {

    private String lineGuid;
    private String lineInfo;
    private String link;
    private String trend;
    private String lineNumber;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

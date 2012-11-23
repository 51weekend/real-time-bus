/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-21.
 */

package com.bustime.common.model;

import com.alibaba.fastjson.JSON;

/**
 * 站台信息.
 *
 * @author chengdong
 */
public class Station {

    private String link;
    private String standCode;
    private String area;
    private String road;
    private String roadSection;
    private String trend;
    private String standName;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getRoadSection() {
        return roadSection;
    }

    public void setRoadSection(String roadSection) {
        this.roadSection = roadSection;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}
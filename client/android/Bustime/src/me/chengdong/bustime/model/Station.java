/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-21.
 */

package me.chengdong.bustime.model;

import org.json.JSONObject;

/**
 * 站台信息.
 * 
 * @author chengdong
 */
public class Station {

    private String standCode;
    private String standName;
    private String road;
    private String roadSection;
    private String area;
    private String trend;
    private String lines;

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

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public void deserialize(JSONObject json) {
        standCode = json.optString("standCode", "");
        standName = json.optString("standName", "");
        road = json.optString("road", "");
        roadSection = json.optString("roadSection", "");
        area = json.optString("area", "");
        trend = json.optString("trend", "");
        lines = json.optString("lines", "");
    }

}
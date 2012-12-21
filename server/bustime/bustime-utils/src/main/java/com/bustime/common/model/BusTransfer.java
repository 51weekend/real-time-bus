/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-20.
 */

package com.bustime.common.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * TODO.
 *
 * @author chengdong
 */
public class BusTransfer {
    private String distance;
    private String title;
    private List<Transfer> keyPolylines;

    private List<Point> keyPoints;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Transfer> getKeyPolylines() {
        return keyPolylines;
    }

    public void setKeyPolylines(List<Transfer> keyPolylines) {
        this.keyPolylines = keyPolylines;
    }

    public List<Point> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(List<Point> keyPoints) {
        this.keyPoints = keyPoints;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

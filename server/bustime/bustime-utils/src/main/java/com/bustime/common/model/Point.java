/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-20.
 */

package com.bustime.common.model;

import com.alibaba.fastjson.JSON;

/**
 * TODO.
 *
 * @author chengdong
 */
public class Point {

    private String scode;
    private Position geometry;
    private String name;

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public Position getGeometry() {
        return geometry;
    }

    public void setGeometry(Position geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}

/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-19.
 */

package me.chengdong.bustime.model;

import org.json.JSONObject;

/**
 * TODO.
 *
 * @author chengdong
 */
public class CodeValue {

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void deserialize(JSONObject json) {
        this.code = json.optString("code");
        this.value = json.optString("value");
    }

}

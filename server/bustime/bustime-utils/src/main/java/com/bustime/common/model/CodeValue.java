/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-19.
 */

package com.bustime.common.model;

/**
 * TODO.
 *
 * @author chengdong
 */
public class CodeValue {

    private String code;
    private String value;

    public CodeValue(String code, String value) {
        super();
        this.code = code;
        this.value = value;
    }

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

}

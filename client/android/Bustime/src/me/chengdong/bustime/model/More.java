/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-31.
 */

package me.chengdong.bustime.model;

/**
 * TODO.
 *
 * @author chengdong
 */
public class More {

    public More(String name, @SuppressWarnings("rawtypes") Class activity) {
        super();
        this.name = name;
        this.activity = activity;
    }

    private String name;
    @SuppressWarnings("rawtypes")
    private Class activity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("rawtypes")
    public Class getActivity() {
        return activity;
    }

    public void setActivity(@SuppressWarnings("rawtypes") Class activity) {
        this.activity = activity;
    }

}

/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-9-5.
 */

package me.chengdong.bustime.meta;

/**
 * 设备类型.
 *
 * @author chengdong
 */
public enum FavoriteType {
    STATION(1, "站点"), LINE(2, "线路");

    private int type;

    private String name;

    FavoriteType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static FavoriteType resolve(int intType) {
        for (FavoriteType type : values()) {
            if (type.getType() == intType) {
                return type;
            }
        }
        return null;
    }

}

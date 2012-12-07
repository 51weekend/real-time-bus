/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-6.
 */

package me.chengdong.bustime.model;

import org.json.JSONObject;

/**
 * TODO.
 *
 * @author chengdong
 */
public class Config {

    private String configKey;
    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public void deserialize(JSONObject json) {
        this.configKey = json.optString("configKey", "");
        this.configValue = json.optString("configValue", "");

    }

}

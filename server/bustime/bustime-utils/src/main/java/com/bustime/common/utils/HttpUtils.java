/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author chengdong
 */
public class HttpUtils {
    public static Map<String, String> getUrlParameters(String url) {

        Map<String, String> parameters = new HashMap<String, String>();
        if (url == null) {
            return parameters;
        }

        url = url.replaceAll("&amp;", "&").replace("=&gt;", "->");
        String[] splitUrl = url.split("\\?");
        if (splitUrl == null || splitUrl.length < 2) {
            return parameters;
        }
        String[] parametersString = splitUrl[1].split("&");
        if (parametersString.length == 0) {
            return parameters;
        }
        for (int i = 0; i < parametersString.length; i++) {
            String[] keyValue = parametersString[i].split("=");
            if (keyValue == null || keyValue.length < 2) {
                continue;
            }
            String key = keyValue[0];
            parameters.put(key.replace(key.charAt(0), Character.toLowerCase(key.charAt(0))), keyValue[1]);

        }

        return parameters;
    }
}

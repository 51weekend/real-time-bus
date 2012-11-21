/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-8-3.
 */

package com.bustime.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类.
 *
 * @author chengdong
 */
public class LoggerUtils {

    private static final Logger error = LoggerFactory.getLogger("com.snda.family.server.looger.error");
    private static final Logger alarm = LoggerFactory.getLogger("com.snda.family.server.looger.alarm");

    public static void error(String errorMessage, Throwable e) {
        error.error(errorMessage, e);
    }

    public static void error(String errorMessage) {
        error.error(errorMessage);
    }

    public static void alarm(String string) {
        alarm.info(string);
    }

}

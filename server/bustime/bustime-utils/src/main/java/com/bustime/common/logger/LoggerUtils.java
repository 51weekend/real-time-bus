/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-8-3.
 */

package com.bustime.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类.
 *
 * @author chengdong
 */
public class LoggerUtils {

    private static final Logger error = LoggerFactory.getLogger("me.chengdong.bustime.looger.error");
    private static final Logger alarm = LoggerFactory.getLogger("me.chengdong.bustime.looger.alarm");
    private static final Logger info = LoggerFactory.getLogger("me.chengdong.bustime.looger.info");

    public static void error(String errorMessage, Throwable e) {
        e.printStackTrace();
        error.error(errorMessage, e);
    }

    public static void error(String errorMessage) {
        error.error(errorMessage);
    }

    public static void info(String infoMessage) {
        info.info(infoMessage);
    }

    public static void alarm(String string) {
        alarm.info(string);
    }

}

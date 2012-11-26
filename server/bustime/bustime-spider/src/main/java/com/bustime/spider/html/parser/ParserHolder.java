/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-26.
 */

package com.bustime.spider.html.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author chengdong
 */
public class ParserHolder {
    public static String LINE_PARSER_KEY = "line";

    @SuppressWarnings("rawtypes")
    private static Map<String, BaseParser> parsers = new HashMap<String, BaseParser>();

    public static void registerParser(String key, @SuppressWarnings("rawtypes") BaseParser parser) {
        parsers.put(key, parser);
    }

    @SuppressWarnings("rawtypes")
    public static BaseParser getParser(String key) {
        return parsers.get(key);
    }

}

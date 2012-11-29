/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.ParserHolder.LINE_PARSER_KEY;
import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.LineUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.Line;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class LineParser extends BaseParser<Line> {

    private StringBuilder url = new StringBuilder();

    public LineParser() {
        colMap.put(0, "link");
        colMap.put(1, "trend");
        try {
            url.append(LineUrl.getUrl()).append("?__EVENTVALIDATION=").append(LineUrl.getEventValidation())
                    .append("&__VIEWSTATE=").append(LineUrl.getViewState()).append("&ctl00$MainContent$SearchLine=")
                    .append(URLEncoder.encode("搜索", UTF8.getEncode())).append("&ctl00$MainContent$LineName=");
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error("init LineParser url error", e);
        }
        ParserHolder.registerParser(LINE_PARSER_KEY, this);

    }

    /**
     * 解析站点
     * @param url
     * @throws Exception
     */
    public String parser(String line) throws Exception {
        String path = url.toString() + URLEncoder.encode(line, UTF8.getEncode());
        Parser parser = new Parser(path);
        parser.setEncoding(UTF8.getEncode());
        List<Map<String, String>> data = parserSpanTable(parser);

        return JSON.toJSONString(data);
    }

    @Override
    public Line parseObject(String jsonString) {
        return JSON.parseObject(jsonString, Line.class);
    }

    public static void main(String args[]) throws Exception {
        LineParser parser = new LineParser();
        List<Line> lines = parser.getData("2");

        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }

    }

}

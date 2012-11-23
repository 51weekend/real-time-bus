/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.LineUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bustime.core.logger.LoggerUtils;
import com.bustime.core.model.Line;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class LineParser extends BaseParser {

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

    public List<Line> getLines(String lineNumber) {
        List<Line> lines = new ArrayList<Line>();
        try {
            String jsonArray = this.parser(lineNumber);
            JSONArray singleLines = JSONArray.parseArray(jsonArray);

            for (int i = 0; i < singleLines.size(); i++) {
                Line line = JSON.parseObject(JSON.toJSONString(singleLines.get(i)), Line.class);
                line.setLineNumber(lineNumber);
                lines.add(line);
            }
        } catch (Exception e) {
            LoggerUtils.error("get the bus line from remote error of lineNumber:" + lineNumber, e);
        }

        return lines;

    }

    public static void main(String args[]) throws Exception {
        LineParser parser = new LineParser();
        List<Line> lines = parser.getLines("快线2");

        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }

    }

}

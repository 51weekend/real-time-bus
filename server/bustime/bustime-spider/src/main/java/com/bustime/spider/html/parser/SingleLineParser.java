/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.SingleLineUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bustime.core.logger.LoggerUtils;
import com.bustime.core.model.SingleLine;

/**
 * TODO.
 *
 * @author chengdong
 */
public class SingleLineParser extends BaseParser {

    public SingleLineParser() {
        colMap.put(0, "link");
        colMap.put(1, "standCode");
        colMap.put(2, "busNumber");
        colMap.put(3, "time");
    }

    /**
     * 根据车次编号,获取车次线路实时信息
     * @param lineCode 车次编号
     * @return
     */
    public List<SingleLine> getSingleLine(String lineCode) {
        List<SingleLine> lines = new ArrayList<SingleLine>();
        try {
            String jsonArray = this.parser(lineCode);
            JSONArray singleLines = JSONArray.parseArray(jsonArray);

            for (int i = 0; i < singleLines.size(); i++) {
                SingleLine line = JSON.parseObject(JSON.toJSONString(singleLines.get(i)), SingleLine.class);
                line.setLineCode(lineCode);
                lines.add(line);
            }
        } catch (Exception e) {
            LoggerUtils.error("get the line data from remote error of line:" + lineCode, e);
        }

        return lines;
    }

    /**
     * 解析站点
     * @param url
     * @throws Exception
     */
    public String parser(String lineGuid) throws Exception {
        Parser parser = new Parser(SingleLineUrl.getUrl() + lineGuid);
        parser.setEncoding(UTF8.getEncode());
        List<Map<String, String>> data = parserSpanTable(parser);

        return JSON.toJSONString(data);
    }

    public static void main(String args[]) throws Exception {
        SingleLineParser parser = new SingleLineParser();
        List<SingleLine> lines = parser.getSingleLine("6b3ad726-d033-422b-ba65-43253011865d");

        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }

    }

}

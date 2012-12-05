/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.BusTime;
import com.bustime.common.utils.HttpUtil;
import com.bustime.common.utils.HttpUtils;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class BusTimeParser {

    public List<Map<String, String>> parserSpanTable(Parser parser, int startRow) throws ParserException {

        TagNameFilter aFilter = new TagNameFilter("th");
        HasAttributeFilter aAttributeFilter = new HasAttributeFilter("rowspan", "2");
        AndFilter aAndFilter = new AndFilter(aFilter, aAttributeFilter);
        NodeList pList = parser.extractAllNodesThatMatch(aAndFilter);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0, size = pList.size(); i < size; i++) {
            Node node = pList.elementAt(i);

            NodeList list = node.getChildren();
            for (int j = 0, n = list.size(); j < n; j++) {
                Node children = list.elementAt(j);
                if (children instanceof LinkTag) {
                    LinkTag link = (LinkTag) children;
                    Map<String, String> parameters = HttpUtils.getUrlParameters(link.getLink());
                    data.add(parameters);
                }
            }

        }

        return data;
    }

    String url = "http://m.sz-map.com/search?paging=y&st=1&kw=";

    public String parser(String lineNumber) throws ParserException {
        String path = url + lineNumber;
        String string = HttpUtil.readStringFromUrl(path, UTF8.getEncode(), "");
        Parser parser = new Parser(string);
        List<Map<String, String>> data = parserSpanTable(parser, 4);
        return JSON.toJSONString(data);
    }

    public BusTime parseObject(String jsonString) {
        return JSON.parseObject(jsonString, BusTime.class);
    }

    public List<BusTime> getData(String parameter) {
        List<BusTime> list = new ArrayList<BusTime>();
        try {
            String jsonArray = this.parser(parameter);
            JSONArray json = JSONArray.parseArray(jsonArray);

            for (int i = 0; i < json.size(); i++) {
                list.add(parseObject(JSON.toJSONString(json.get(i))));
            }
        } catch (Exception e) {
            LoggerUtils.error("get the data from remote error of parameter:" + parameter, e);
        }
        return list;
    }

    public static void main(String args[]) throws Exception {
        BusTimeParser parser = new BusTimeParser();
        List<BusTime> busTimes = parser.getData("117");
        for (int i = 0; i < busTimes.size(); i++) {
            System.out.println(busTimes.get(i));
        }
    }

}

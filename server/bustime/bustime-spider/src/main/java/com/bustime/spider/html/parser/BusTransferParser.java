/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-20.
 */

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.BusTransfer;
import com.bustime.common.utils.HttpUtil;

/**
 * TODO.
 *
 * @author chengdong
 */
public class BusTransferParser {

    String url = "http://www.sz-map.com/maps/ts?routeType=&routeSearchType=BusTransfer&research=0&schemaCount=5&region=S&fromType=1&toType=1&m=srbfat&fromValue=402882b82d25a5fa012d25db8c385f4f&toValue=402882b82d25a5fa012d25db8c29597d";

    public JSONArray parser(String from, String to) throws ParserException {
        String path = url;
        String string = HttpUtil.readStringFromUrl(path, UTF8.getEncode(), "");
        JSONObject json = JSONObject.parseObject(string);
        return json.getJSONArray("schemas");
    }

    public BusTransfer parseObject(String jsonString) {
        return JSON.parseObject(jsonString, BusTransfer.class);
    }

    public List<BusTransfer> getData(String from, String to) {
        List<BusTransfer> list = new ArrayList<BusTransfer>();
        try {
            JSONArray json = this.parser(from, to);

            for (int i = 0; i < json.size(); i++) {
                list.add(parseObject(JSON.toJSONString(json.get(i))));
            }
        } catch (Exception e) {
            LoggerUtils.error("get the data from remote error ", e);
        }
        return list;
    }

    public static void main(String args[]) throws Exception {
        BusTransferParser parser = new BusTransferParser();
        List<BusTransfer> busTimes = parser.getData("", "");
        for (int i = 0; i < busTimes.size(); i++) {
            System.out.println(busTimes.get(i));
        }

        System.out.println(URLEncoder.encode("海悦花园四区"));
    }

}

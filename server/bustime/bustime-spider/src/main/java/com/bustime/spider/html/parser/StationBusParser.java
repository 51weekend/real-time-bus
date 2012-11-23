package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.StationBusUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.StationBus;

/**
 * 
 * 经过该站点的车辆.
 *
 * @author chengdong
 */
public class StationBusParser extends BaseParser {

    public StationBusParser() {
        colMap.put(0, "link");
        colMap.put(1, "trend");
        colMap.put(2, "busNumber");
        colMap.put(3, "time");
        colMap.put(4, "standNum");

    }

    public String parser(String standCode) throws ParserException {
        String url = StationBusUrl.getUrl() + standCode;
        Parser parser = new Parser(url);
        parser.setEncoding(UTF8.getEncode());
        List<Map<String, String>> data = parserSpanTable(parser);
        return JSON.toJSONString(data);
    }

    public List<StationBus> getStationBus(String stationCode) {
        List<StationBus> stationBuses = new ArrayList<StationBus>();
        try {
            String jsonArray = this.parser(stationCode);
            JSONArray singleLines = JSONArray.parseArray(jsonArray);

            for (int i = 0; i < singleLines.size(); i++) {
                StationBus stationBus = JSON.parseObject(JSON.toJSONString(singleLines.get(i)), StationBus.class);
                stationBus.setStandCode(stationCode);
                stationBuses.add(stationBus);
            }
        } catch (Exception e) {
            LoggerUtils.error("get the stationBuses from remote error of stationCode:" + stationCode, e);
        }
        return stationBuses;
    }

    public static void main(String args[]) throws Exception {
        StationBusParser parser = new StationBusParser();
        List<StationBus> stationBuses = parser.getStationBus("CVZ");
        for (int i = 0; i < stationBuses.size(); i++) {
            System.out.println(stationBuses.get(i));
        }
    }

}

package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.StationBusUrl;

import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bustime.common.model.StationBus;

/**
 * 
 * 经过该站点的车辆.
 *
 * @author chengdong
 */
@Service
public class StationBusParser extends BaseParser<StationBus> {

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
        List<Map<String, String>> data = parserSpanTable(parser, 1);
        return JSON.toJSONString(data);
    }

    @Override
    public StationBus parseObject(String jsonString) {
        return JSON.parseObject(jsonString, StationBus.class);
    }

    public static void main(String args[]) throws Exception {
        StationBusParser parser = new StationBusParser();
        List<StationBus> stationBuses = parser.getData("CVZ");
        for (int i = 0; i < stationBuses.size(); i++) {
            System.out.println(stationBuses.get(i));
        }
    }

}

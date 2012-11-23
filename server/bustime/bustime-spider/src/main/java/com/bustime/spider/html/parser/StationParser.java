package com.bustime.spider.html.parser;

import static com.bustime.spider.html.parser.meta.CharacterEncode.UTF8;
import static com.bustime.spider.html.parser.meta.ParserUrl.StationUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.Station;

/**
 * 
 * 站点内容分析.
 *
 * @author chengdong
 */
public class StationParser extends BaseParser {

    private String url;

    public StationParser() {
        colMap.put(0, "link");
        colMap.put(1, "standCode");
        colMap.put(2, "area");
        colMap.put(3, "road");
        colMap.put(4, "roadSection");
        colMap.put(5, "trend");

        try {
            url = StationUrl.getUrl() + "?__EVENTVALIDATION=" + StationUrl.getEventValidation() + "&__VIEWSTATE="
                    + StationUrl.getViewState() + "&ctl00$MainContent$SearchCode="
                    + URLEncoder.encode("搜索", UTF8.getEncode()) + "&ctl00$MainContent$StandName=";
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error("init StationParser url error", e);
        }
    }

    /**
     * 解析站点
     * @param url
     * @throws Exception
     */
    public String parser(String name) throws Exception {
        String path = url + URLEncoder.encode(name, UTF8.getEncode());
        Parser parser = new Parser(path);
        parser.setEncoding(UTF8.getEncode());
        List<Map<String, String>> data = parserSpanTable(parser);
        return JSON.toJSONString(data);
    }

    public List<Station> getStationByName(String stationName) {
        List<Station> stations = new ArrayList<Station>();
        try {
            String jsonArray = this.parser(stationName);
            JSONArray singleLines = JSONArray.parseArray(jsonArray);

            for (int i = 0; i < singleLines.size(); i++) {
                Station station = JSON.parseObject(JSON.toJSONString(singleLines.get(i)), Station.class);
                stations.add(station);
            }
        } catch (Exception e) {
            LoggerUtils.error("get the station from remote error of name:" + stationName, e);
        }

        return stations;
    }

    public static void main(String args[]) throws Exception {
        StationParser parser = new StationParser();
        List<Station> stations = parser.getStationByName("莲花新村");
        for (int i = 0; i < stations.size(); i++) {
            System.out.println(stations.get(i));
        }
    }

}

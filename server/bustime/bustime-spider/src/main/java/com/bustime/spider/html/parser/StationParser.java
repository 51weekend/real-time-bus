package com.bustime.spider.html.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import com.alibaba.fastjson.JSON;
import com.bustime.core.logger.LoggerUtils;

/**
 * 
 * 站点内容分析.
 *
 * @author chengdong
 */
public class StationParser {

    private static Map<Integer, String> colMap = new HashMap<Integer, String>();
    private static String url;

    static {
        colMap.put(0, "stationName");
        colMap.put(1, "stationCode");
        colMap.put(2, "area");
        colMap.put(3, "road");
        colMap.put(4, "roadSection");
        colMap.put(5, "trend");

        try {
            url = "http://www.szjt.gov.cn/apts/default.aspx?__EVENTVALIDATION="
                    + URLEncoder.encode("/wEWBQLi9afBDgLq+uyKCAKkmJj/DwL0+sTIDgLl5vKEDpw94uqehgsgP25TIVg5cQBBQZj0",
                            "utf-8") + "&__VIEWSTATE="
                    + URLEncoder.encode("/wEPDwULLTE5ODM5MjcxNzlkZAPmAlFZ6B58iSScBgX0og8wpbih", "utf-8")
                    + "&ctl00$MainContent$SearchCode=" + URLEncoder.encode("搜索", "utf-8")
                    + "&ctl00$MainContent$StandName=";
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error("init StationParser url error", e);
        }
    }

    public static void main(String args[]) throws Exception {
        StationParser parser = new StationParser();
        System.out.println(parser.parser("海悦花园"));

    }

    /**
     * 解析站点
     * @param url
     * @throws Exception
     */
    public String parser(String name) throws Exception {
        url += URLEncoder.encode(name, "utf-8");
        Parser parser = new Parser(url);
        parser.setEncoding("utf8");
        TagNameFilter aFilter = new TagNameFilter("span");
        HasChildFilter aChildFilter = new HasChildFilter(new TagNameFilter("table"));
        AndFilter aAndFilter = new AndFilter(aFilter, aChildFilter);
        NodeList pList = parser.extractAllNodesThatMatch(aAndFilter);
        Node tag = pList.elements().nextNode();

        if (!(tag instanceof Span)) {
            return null;
        }
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        NodeList table = tag.getChildren();
        for (int i = 0; i < table.size(); i++) {
            Node node = table.elementAt(i);
            if (!(node instanceof TableTag)) {
                continue;
            }
            NodeList tableRows = node.getChildren();

            for (int j = 1; j < tableRows.size(); j++) {
                TableRow row = (TableRow) tableRows.elementAt(j);
                TableColumn[] cols = row.getColumns();
                Map<String, String> rowData = new HashMap<String, String>();
                for (int k = 0; k < cols.length; k++) {
                    Node n = cols[k].getChildren().elementAt(0);
                    if (n instanceof LinkTag) {
                        rowData.put(colMap.get(k), ((LinkTag) n).getLinkText());
                    } else {
                        rowData.put(colMap.get(k), n.getText());
                    }
                }
                data.add(rowData);

            }
        }

        return JSON.toJSONString(data);
    }

}

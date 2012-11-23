/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.spider.html.parser;

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
import org.htmlparser.util.ParserException;

import com.bustime.common.utils.HttpUtils;

/**
 * TODO.
 *
 * @author chengdong
 */
public class BaseParser {

    protected Map<Integer, String> colMap = new HashMap<Integer, String>();

    public List<Map<String, String>> parserSpanTable(Parser parser) throws ParserException {
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
                    if (cols[k].getChildren() == null) {
                        continue;
                    }
                    Node n = cols[k].getChildren().elementAt(0);
                    if (n instanceof LinkTag) {
                        rowData.putAll(HttpUtils.getUrlParameters(((LinkTag) n).getLink()));
                        rowData.put(colMap.get(k), ((LinkTag) n).getLink());
                    } else {
                        rowData.put(colMap.get(k), n.getText());
                    }
                }
                data.add(rowData);

            }
        }
        return data;
    }

}

/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-23.
 */

package com.bustime.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bustime.common.model.Line;
import com.bustime.common.model.SingleLine;
import com.bustime.common.model.StationBus;
import com.bustime.core.dao.MybatisBaseDao;
import com.bustime.spider.html.parser.LineParser;
import com.bustime.spider.html.parser.SingleLineParser;
import com.bustime.spider.html.parser.StationBusParser;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class ApiService {

    @Autowired
    private LineParser lineParser;

    @Autowired
    private MybatisBaseDao<Line> lineDao;

    @Autowired
    private SingleLineParser singleLineParser;

    @Autowired
    private StationBusParser stationBusParser;

    public List<Line> queryLine(String lineNumber) {
        List<Line> lines = lineDao.selectList("queryLine", lineNumber);
        if (CollectionUtils.isEmpty(lines)) {
            lines = lineParser.getData(lineNumber);
            // TODO 这里可以异步来做,通过生产消费的方式、采用队列
            for (Line line : lines) {
                lineDao.save("saveLine", line);
            }
        }
        return lines;
    }

    /**
     * 根据线路编号查询车辆、站台信息
     * @param lineCode 如:6b3ad726-d033-422b-ba65-43253011865d
     * @return
     */
    public List<SingleLine> querySingleLine(String lineCode) {
        return singleLineParser.getData(lineCode);
    }

    public List<StationBus> getStationBus(String stationCode) {
        return stationBusParser.getData(stationCode);
    }

}

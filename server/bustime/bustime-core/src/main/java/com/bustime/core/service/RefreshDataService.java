/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-5.
 */

package com.bustime.core.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.BusTime;
import com.bustime.common.model.Line;
import com.bustime.common.model.SingleLine;
import com.bustime.core.dao.MybatisBaseDao;
import com.bustime.spider.html.parser.BusTimeParser;
import com.bustime.spider.html.parser.LineParser;
import com.bustime.spider.html.parser.StationBusParser;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class RefreshDataService {

    @Autowired
    private LineParser lineParser;

    @Autowired
    private StationBusParser stationBusParser;

    @Autowired
    private MybatisBaseDao<Line> lineDao;

    @Autowired
    ApiService apiService;

    @Autowired
    BusTimeParser busTimeParser;

    public void refresh() {
        LoggerUtils.info("refresh start");
        updateLineRunTime();
        updateAllData();
        LoggerUtils.info("refresh end");
    }

    private void updateLineRunTime() {
        try {
            List<Line> list = lineDao.selectList("queryNoRunTimeLine", null);
            for (Line line : list) {
                if (StringUtils.isEmpty(line.getRunTime())) {
                    List<BusTime> bustimes = busTimeParser.getData(line.getLineNumber());
                    for (BusTime time : bustimes) {
                        lineDao.update("updateLineRunTime", time);
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtils.error("refresh line run time error", e);
        }
    }

    private void updateAllData() {
        List<Line> lineList = lineDao.selectList("queryLine", null);
        for (Line line : lineList) {
            List<SingleLine> singleLineList = apiService.querySingleLine(line.getLineGuid());
            for (SingleLine singleLine : singleLineList) {
                apiService.queryStation(singleLine.getStandName());
                if (StringUtils.isEmpty(singleLine.getStandCode())) {
                    continue;
                }
                apiService.queryStationBus(singleLine.getStandCode());
            }
        }
    }
}

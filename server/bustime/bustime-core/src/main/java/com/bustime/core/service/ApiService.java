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
import com.bustime.common.model.Station;
import com.bustime.common.model.StationBus;
import com.bustime.core.dao.MybatisBaseDao;
import com.bustime.spider.html.parser.LineParser;
import com.bustime.spider.html.parser.SingleLineParser;
import com.bustime.spider.html.parser.StationBusParser;
import com.bustime.spider.html.parser.StationParser;

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

    @Autowired
    private StationParser stationParser;

    // TODO 服务里面的方法都可以重构.将这些parser注册到总线、根据请求获取对应实现
    /**
     * 根据线路名称 查询该线路车行驶区间及线路编号.
     * @param lineNumber 线路名称 如:快线2、 117
     * @return
     */
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
     * 根据线路编号查询该线路公交车经过的所有站台、以及整个线路上所有车所在站台及进站时间.
     * @param lineCode 线路编号  如:6b3ad726-d033-422b-ba65-43253011865d
     * @return
     */
    public List<SingleLine> querySingleLine(String lineCode) {
        // 可以考虑后台定时(30s)对所有线路运行车辆的实时信息爬取一次、有变更的话、服务端push给客户端、或者客户端从服务端读取.
        return singleLineParser.getData(lineCode);
    }

    /**
     * 根据站台编号，获取经过该站点的所有公交线路、以及距离公交距离该站台的站数.
     * @param stationCode 站台编号 如:CVZ
     * @return
     */
    public List<StationBus> queryStationBus(String stationCode) {
        return stationBusParser.getData(stationCode);
    }

    /**
     * 根据站台名称，查询站台.
     * @param stationName 站台名称 如:莲花新村四区
     * @return
     */
    public List<Station> queryStation(String stationName) {
        return stationParser.getData(stationName);
    }

}

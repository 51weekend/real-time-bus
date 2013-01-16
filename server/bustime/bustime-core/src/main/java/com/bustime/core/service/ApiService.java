/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-23.
 */

package com.bustime.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.model.Config;
import com.bustime.common.model.Line;
import com.bustime.common.model.SingleLine;
import com.bustime.common.model.Station;
import com.bustime.common.model.StationBus;
import com.bustime.core.dao.MybatisBaseDao;
import com.bustime.spider.html.parser.BusTimeParser;
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
    private SingleLineParser singleLineParser;

    @Autowired
    private StationBusParser stationBusParser;

    @Autowired
    private StationParser stationParser;

    @Autowired
    private MybatisBaseDao<Config> configDao;

    @Autowired
    private MybatisBaseDao<Line> lineDao;

    @Autowired
    private MybatisBaseDao<SingleLine> singleLineDao;

    @Autowired
    private MybatisBaseDao<StationBus> stationBusDao;

    @Autowired
    private MybatisBaseDao<Station> stationDao;

    @Autowired
    BusTimeParser busTimeParser;

    private ThreadPoolExecutor executor;

    private int threads = 4;
    private int queueSize = 1000;

    @PostConstruct
    public void init() {

        executor = new ThreadPoolExecutor(threads, threads, Long.MAX_VALUE, TimeUnit.NANOSECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
        executor.prestartCoreThread();
    }

    // TODO 服务里面的方法都可以重构.将这些parser注册到总线、根据请求获取对应实现
    /**
     * 根据线路名称 查询该线路车行驶区间及线路编号.
     * @param lineNumber 线路名称 如:快线2、 117
     * @return
     */
    public List<Line> queryLine(String lineNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lineNumber", "%" + lineNumber + "%");
        List<Line> lines = lineDao.selectList("queryLine", parameters);
        if (!CollectionUtils.isEmpty(lines)) {
            return filterLines(lineNumber, lines);
        }

        lines = lineParser.getData(lineNumber);
        final List<Line> saveData = new ArrayList<Line>();
        for (Line line : lines) {
            line.setLineInfo(line.getLineInfo());
            line.setTrend(line.getTrend());
            saveData.add(line);
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Line line : saveData) {
                        List<Line> exist = lineDao.selectList("queryLineByGuid", line.getLineGuid());
                        if (CollectionUtils.isEmpty(exist)) {
                            lineDao.save("saveLine", line);
                        }

                    }
                } catch (Exception e) {
                    LoggerUtils.error("save line error", e);
                }
            }
        });

        return filterLines(lineNumber, lines);
    }

    private List<Line> filterLines(String lineNumber, List<Line> lines) {
        List<Line> result = new ArrayList<Line>();
        for (Line line : lines) {
            // 远程爬的数据、支持模糊查询、去掉多余的数据
            // 过滤、如 输入 2 像 快线路2号 2路 2 这样的返回 想812 216 这样的就不返回
            if (isNumeric(line.getLineNumber()) && !line.getLineNumber().equals(lineNumber)) {
                continue;
            }
            result.add(line);
        }
        return result;
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据线路编号查询该线路公交车经过的所有站台、以及整个线路上所有车所在站台及进站时间.
     * @param lineCode 线路编号  如:6b3ad726-d033-422b-ba65-43253011865d
     * @return
     */
    public List<SingleLine> querySingleLine(final String lineCode) {
        return singleLineParser.getData(lineCode);
    }

    public List<SingleLine> downloadSingleLine(final String lineCode) {
        final List<SingleLine> data = singleLineParser.getData(lineCode);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SingleLine> dbData = singleLineDao.selectList("querySingleLine", lineCode);
                    if (CollectionUtils.isEmpty(dbData)) {
                        for (SingleLine single : data) {
                            if (StringUtils.isEmpty(single.getStandCode())) {
                                continue;
                            }
                            singleLineDao.save("saveSingleLine", single);
                        }
                    }
                    updateLine(data);
                } catch (Exception e) {
                    LoggerUtils.error("save SingleLine error", e);
                }
            }
        });
        return data;
    }

    private void updateLine(List<SingleLine> lineInfo) {
        if (CollectionUtils.isEmpty(lineInfo)) {
            return;
        }
        List<Line> data = lineDao.selectList("queryLineByGuid", lineInfo.get(0).getLineGuid());
        if (!CollectionUtils.isEmpty(data)) {
            if (data.get(0).getTotalStation() > 0) {
                return;
            }
        }
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lineGuid", lineInfo.get(0).getLineGuid());
        parameters.put("startStation", lineInfo.get(0).getStandName());
        parameters.put("endStation", lineInfo.get(lineInfo.size() - 1).getStandName());
        parameters.put("totalStation", lineInfo.size());
        lineDao.update("updateLine", parameters);
    }

    /**
     * 根据站台编号，获取经过该站点的所有公交线路、以及距离公交距离该站台的站数.
     * @param stationCode 站台编号 如:CVZ
     * @return
     */
    public List<StationBus> queryStationBus(final String stationCode) {
        final List<StationBus> stationBuses = stationBusParser.getData(stationCode);
        if (CollectionUtils.isEmpty(stationBuses)) {
            return stationBuses;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = stationBuses.size(); i < n; i++) {
            sb.append("'").append(stationBuses.get(i).getLineGuid()).append("'");
            if (i < n - 1) {
                sb.append(",");
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("lineGuid", sb.toString());
        List<Line> lines = lineDao.selectList("queryLinesByGuid", map);
        for (Line line : lines) {
            for (StationBus stationBus : stationBuses) {

                if (line.getLineGuid().equals(stationBus.getLineGuid())) {
                    stationBus.setStartStation(line.getStartStation());
                    stationBus.setEndStation(line.getEndStation());
                }
            }
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                for (StationBus staBus : stationBuses) {
                    sb.append(staBus.getLineNumber()).append(",");
                    try {
                        List<StationBus> dbData = stationBusDao.selectList("queryStationBus", staBus);
                        if (CollectionUtils.isEmpty(dbData)) {
                            stationBusDao.save("saveStationBus", staBus);
                        }
                    } catch (Exception e) {
                        LoggerUtils.error("save stationBus error lineGuid:" + staBus.getLineGuid()
                                + "*****  standCode:" + staBus.getStandCode() + "*****", e);
                    }
                }
                // TODO 更新该站台经过的车辆、只在正常时间段内 如 10:00 到下午6：00
                String lines = sb.toString();
                if (StringUtils.isEmpty(lines)) {
                    return;
                }
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("standCode", stationCode);
                parameters.put("bus", lines.substring(0, lines.length() - 1));
                stationDao.update("updateStationLines", parameters);

            }
        });
        return stationBuses;
    }

    /**
     * 根据站台名称，查询站台.
     * @param stationName 站台名称 如:莲花新村四区
     * @return
     */
    public List<Station> queryStation(String stationName) {
        List<Station> stations = stationDao.selectList("queryStationByName", "%" + stationName + "%");
        if (!CollectionUtils.isEmpty(stations)) {
            return stations;
        }

        return new ArrayList<Station>();

    }

    public void downloadStation(String stationName) {
        final List<Station> parserStations = stationParser.getData(stationName);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (CollectionUtils.isEmpty(parserStations)) {
                        return;
                    }
                    for (Station station : parserStations) {
                        List<Station> dbData = stationDao.selectList("queryStationByCode", station.getStandCode());
                        if (CollectionUtils.isEmpty(dbData)) {
                            stationBusDao.save("saveStation", station);
                        }
                    }
                } catch (Exception e) {
                    LoggerUtils.error("save station error", e);
                }
            }
        });
    }

    public List<Station> queryAllStation() {
        // TODO 数据以后存在内存中、直接从内存读取
        return stationDao.selectList("queryAllStation", null);

    }

    public Config queryConfigByKey(String key) {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("key", key);
        return configDao.selectOne("queryConfig", parameter);
    }

    public List<Config> queryConfigByType(String type) {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("type", type);
        return configDao.selectList("queryConfig", parameter);
    }

}

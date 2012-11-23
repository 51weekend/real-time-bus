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
import com.bustime.core.dao.MybatisBaseDao;
import com.bustime.spider.html.parser.LineParser;

/**
 * TODO.
 *
 * @author chengdong
 */
@Service
public class LineService {

    @Autowired
    private LineParser lineParser;
    @Autowired
    private MybatisBaseDao<Line> lineDao;

    public List<Line> getLine(String lineNumber) {
        List<Line> lines = lineDao.selectList("queryLine", lineNumber);
        if (CollectionUtils.isEmpty(lines)) {
            lines = lineParser.getLines(lineNumber);
            // TODO 这里可以异步来做,通过生产消费的方式、采用队列
            for (Line line : lines) {
                lineDao.save("saveLine", line);
            }
        }
        return lines;
    }

}
